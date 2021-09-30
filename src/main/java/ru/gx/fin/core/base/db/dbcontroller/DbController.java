package ru.gx.fin.core.base.db.dbcontroller;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import ru.gx.data.InvalidDataObjectTypeException;
import ru.gx.fin.core.base.db.converters.DerivativeEntityFromDtoConverter;
import ru.gx.fin.core.base.db.converters.PlaceDtoFromEntityConverter;
import ru.gx.fin.core.base.db.converters.SecurityEntityFromDtoConverter;
import ru.gx.fin.core.base.db.dto.Derivative;
import ru.gx.fin.core.base.db.dto.Security;
import ru.gx.fin.core.base.db.entities.DerivativeEntitiesPackage;
import ru.gx.fin.core.base.db.entities.SecurityEntitiesPackage;
import ru.gx.fin.core.base.db.events.LoadedDerivativesEvent;
import ru.gx.fin.core.base.db.events.LoadedSecuritiesEvent;
import ru.gx.fin.core.base.db.memdata.InstrumentTypesMemoryRepository;
import ru.gx.fin.core.base.db.memdata.PlacesMemoryRepository;
import ru.gx.fin.core.base.db.memdata.ProviderTypesMemoryRepository;
import ru.gx.fin.core.base.db.memdata.ProvidersMemoryRepository;
import ru.gx.fin.core.base.db.repository.*;
import ru.gx.kafka.PartitionOffset;
import ru.gx.kafka.load.SimpleIncomeTopicsLoader;
import ru.gx.kafka.load.SimpleKafkaIncomeOffsetsController;
import ru.gx.kafka.upload.EntitiesUploader;
import ru.gx.worker.SimpleIterationExecuteEvent;
import ru.gx.worker.SimpleStartingExecuteEvent;
import ru.gx.worker.SimpleStoppingExecuteEvent;
import ru.gx.worker.SimpleWorker;

import javax.persistence.EntityManagerFactory;

import static lombok.AccessLevel.PROTECTED;

@Slf4j
public class DbController {
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Fields">
    @Getter
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private SimpleWorker simpleWorker;

    @Getter
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private DbControllerSettingsContainer settings;

    @Getter
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private SimpleIncomeTopicsLoader incomeTopicsLoader;

    @Getter
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private SimpleKafkaIncomeOffsetsController simpleKafkaIncomeOffsetsController;

    @Getter
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private EntitiesUploader entitiesUploader;

    @Getter
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private EntityManagerFactory entityManagerFactory;

    @Getter
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private SecurityEntityFromDtoConverter securityEntityFromDtoConverter;

    @Getter
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private DerivativeEntityFromDtoConverter derivativeEntityFromDtoConverter;

    @Getter
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private SecuritiesRepository securitiesRepository;

    @Getter
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private DerivativesRepository derivativesRepository;

    private SessionFactory sessionFactory;
    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Обработка событий Worker-а">

    /**
     * Обработка события о начале работы цикла итераций.
     *
     * @param event Объект-событие с параметрами.
     */
    @SuppressWarnings("unused")
    @EventListener(SimpleStartingExecuteEvent.class)
    public void startingExecute(SimpleStartingExecuteEvent event) throws Exception {
        log.info("Starting startingExecute()");
        if (this.sessionFactory == null) {
            if (entityManagerFactory.unwrap(SessionFactory.class) == null) {
                throw new NullPointerException("entityManagerFactory is not a hibernate factory!");
            }
            this.sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        }

        this.simpleKafkaIncomeOffsetsController.seekIncomeOffsetsOnStart();

        publishAllOnStart();

        log.info("Finished startingExecute()");
    }

    protected void publishAllOnStart() throws Exception {
        for (var uploadDescriptor : this.entitiesUploader.getAllDescriptors()) {
            publishSnapshot(uploadDescriptor.getTopic());
        }
    }

    /**
     * Обработка события об окончании работы цикла итераций.
     *
     * @param event Объект-событие с параметрами.
     */
    @SuppressWarnings("unused")
    @EventListener(SimpleStoppingExecuteEvent.class)
    public void stoppingExecute(SimpleStoppingExecuteEvent event) {
        log.debug("Starting stoppingExecute()");
        log.debug("Finished stoppingExecute()");
    }

    /**
     * Обработчик итераций.
     *
     * @param event Объект-событие с параметрами итерации.
     */
    @EventListener(SimpleIterationExecuteEvent.class)
    public void iterationExecute(SimpleIterationExecuteEvent event) {
        log.debug("Starting iterationExecute()");
        try {
            this.simpleWorker.runnerIsLifeSet();
            event.setImmediateRunNextIteration(false);

            final var session = this.sessionFactory.openSession();
            try (session) {
                final var tran = session.beginTransaction();
                try {
                    final var durationOnPoll = this.settings.getDurationOnPollMs();
                    this.incomeTopicsLoader.processAllTopics(durationOnPoll);

                    this.simpleKafkaIncomeOffsetsController.saveKafkaOffsets();

                    tran.commit();
                } catch (Exception e) {
                    tran.rollback();
                    internalTreatmentExceptionOnDataRead(event, e);
                }
            }
        } catch (Exception e) {
            internalTreatmentExceptionOnDataRead(event, e);
        } finally {
            log.debug("Finished iterationExecute()");
        }
    }

    /**
     * Обработка ошибки при выполнении итерации.
     *
     * @param event Объект-событие с параметрами итерации.
     * @param e     Ошибка, которую требуется обработать.
     */
    private void internalTreatmentExceptionOnDataRead(SimpleIterationExecuteEvent event, Exception e) {
        log.error("", e);
        if (e instanceof InterruptedException) {
            log.info("event.setStopExecution(true)");
            event.setStopExecution(true);
        } else {
            log.info("event.setNeedRestart(true)");
            event.setNeedRestart(true);
        }
    }

    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Обработка событий о чтении данных из Kafka">

    /**
     * Обработка события о загрузке из Kafka набора объектов {@link Security}.
     *
     * @param event Объект-событие с параметрами.
     */
    @EventListener(LoadedSecuritiesEvent.class)
    public void loadedSecurities(LoadedSecuritiesEvent event) throws InvalidDataObjectTypeException {
        log.debug("Starting loadedSecurities()");
        try {
            this.simpleWorker.runnerIsLifeSet();

            final var securityEntitiesPackage = new SecurityEntitiesPackage();
            this.securityEntityFromDtoConverter.fillEntitiesPackageFromDtoPackage(securityEntitiesPackage, event.getObjects());
            final var started = System.currentTimeMillis();
            this.securitiesRepository.saveAll(securityEntitiesPackage.getObjects());
            log.info("Securities: saved {} rows in {} ms", securityEntitiesPackage.size(), System.currentTimeMillis() - started);
        } finally {
            log.debug("Finished loadedSecurities()");
        }
    }


    /**
     * Обработка события о загрузке из Kafka набора объектов {@link Derivative}.
     *
     * @param event Объект-событие с параметрами.
     */
    @EventListener(LoadedDerivativesEvent.class)
    public void loadedDerivatives(LoadedDerivativesEvent event) throws InvalidDataObjectTypeException {
        log.debug("Starting loadedDerivatives()");
        try {
            this.simpleWorker.runnerIsLifeSet();

            final var derivativeEntitiesPackage = new DerivativeEntitiesPackage();
            this.derivativeEntityFromDtoConverter.fillEntitiesPackageFromDtoPackage(derivativeEntitiesPackage, event.getObjects());
            final var started = System.currentTimeMillis();
            this.derivativesRepository.saveAll(derivativeEntitiesPackage.getObjects());
            log.info("Securities: saved {} rows in {} ms", derivativeEntitiesPackage.size(), System.currentTimeMillis() - started);
        } finally {
            log.debug("Finished loadedDerivatives()");
        }
    }

    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Вспомогательные сервисы">
    public void publishSnapshot(@NotNull final String topic) throws Exception {
        this.entitiesUploader.publishSnapshot(topic);
    }

    @NotNull
    public PartitionOffset getOffset(@NotNull final String topic) {
        final var offset = this.entitiesUploader.getOffset(topic);
        if (offset == null) {
            return new PartitionOffset(0, 0);
        } else {
            return new PartitionOffset(offset.getPartition(), offset.getStartOffset());
        }
    }
    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
}
