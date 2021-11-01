package ru.gx.fin.base.db.dbcontroller;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import ru.gx.data.ActiveConnectionsContainer;
import ru.gx.data.InvalidDataObjectTypeException;
import ru.gx.fin.base.db.converters.DerivativeEntityFromDtoConverter;
import ru.gx.fin.base.db.converters.SecurityEntityFromDtoConverter;
import ru.gx.fin.base.db.dto.Derivative;
import ru.gx.fin.base.db.dto.Security;
import ru.gx.fin.base.db.entities.DerivativeEntitiesPackage;
import ru.gx.fin.base.db.entities.SecurityEntitiesPackage;
import ru.gx.fin.base.db.events.LoadedDerivativesEvent;
import ru.gx.fin.base.db.events.LoadedSecuritiesEvent;
import ru.gx.fin.base.db.repository.DerivativesRepository;
import ru.gx.fin.base.db.repository.SecuritiesRepository;
import ru.gx.kafka.TopicDirection;
import ru.gx.kafka.offsets.PartitionOffset;
import ru.gx.kafka.load.IncomeTopicsOffsetsController;
import ru.gx.kafka.load.SimpleIncomeTopicsConfiguration;
import ru.gx.kafka.load.StandardIncomeTopicsLoader;
import ru.gx.kafka.offsets.TopicsOffsetsLoader;
import ru.gx.kafka.offsets.TopicsOffsetsSaver;
import ru.gx.kafka.upload.SimpleOutcomeTopicsConfiguration;
import ru.gx.kafka.upload.StandardOutcomeTopicsUploader;
import ru.gx.worker.SimpleOnIterationExecuteEvent;
import ru.gx.worker.SimpleOnStartingExecuteEvent;
import ru.gx.worker.SimpleOnStoppingExecuteEvent;
import ru.gx.worker.SimpleWorker;

import javax.sql.DataSource;

import static lombok.AccessLevel.PROTECTED;

@Slf4j
public class DbController {
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Fields">
    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private DataSource dataSource;

    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private ActiveConnectionsContainer connectionsContainer;

    @Getter
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private SimpleWorker simpleWorker;

    @Getter
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private DbControllerSettingsContainer settings;

    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private SimpleIncomeTopicsConfiguration incomeTopicsConfiguration;

    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private StandardIncomeTopicsLoader incomeTopicsLoader;

    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private IncomeTopicsOffsetsController incomeTopicsOffsetsController;

    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private SimpleOutcomeTopicsConfiguration outcomeTopicsConfiguration;

    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private StandardOutcomeTopicsUploader outcomeTopicsUploader;

    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private TopicsOffsetsLoader topicsOffsetsLoader;

    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private TopicsOffsetsSaver topicsOffsetsSaver;

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
    @EventListener(SimpleOnStartingExecuteEvent.class)
    public void startingExecute(SimpleOnStartingExecuteEvent event) throws Exception {
        log.debug("Starting startingExecute()");

        try (var connection = getDataSource().getConnection()) {
            this.connectionsContainer.putCurrent(connection);
            final var offsets = this.topicsOffsetsLoader.loadOffsets(TopicDirection.In, this.incomeTopicsConfiguration.getReaderName());
            if (offsets.size() <= 0) {
                this.incomeTopicsOffsetsController.seekAllToBegin(this.incomeTopicsConfiguration);
            } else {
                this.incomeTopicsOffsetsController.seekTopicsByList(this.incomeTopicsConfiguration, offsets);
            }
        } finally {
            this.connectionsContainer.putCurrent(null);
        }

        publishAllOnStart();

        log.debug("Finished startingExecute()");

    }

    protected void publishAllOnStart() throws Exception {
        // TODO: Сделать логику выгрузки
    }

    /**
     * Обработка события об окончании работы цикла итераций.
     *
     * @param event Объект-событие с параметрами.
     */
    @SuppressWarnings("unused")
    @EventListener(SimpleOnStoppingExecuteEvent.class)
    public void stoppingExecute(SimpleOnStoppingExecuteEvent event) {
        log.debug("Starting stoppingExecute()");
        log.debug("Finished stoppingExecute()");
    }

    /**
     * Обработчик итераций.
     *
     * @param event Объект-событие с параметрами итерации.
     */
    @EventListener(SimpleOnIterationExecuteEvent.class)
    public void iterationExecute(SimpleOnIterationExecuteEvent event) {
        log.debug("Starting iterationExecute()");
        try {
            this.simpleWorker.runnerIsLifeSet();
            event.setImmediateRunNextIteration(false);

            try (var connection = getDataSource().getConnection()) {
                this.connectionsContainer.putCurrent(connection);
                try {
                    final var durationOnPoll = this.settings.getDurationOnPollMs();
                    // Загружаем данные и сохраняем в БД
                    final var result = this.incomeTopicsLoader.processAllTopics(this.incomeTopicsConfiguration, durationOnPoll);
                    for (var c: result.values()) {
                        if (c.size() > 1) {
                            event.setImmediateRunNextIteration(true);
                            break;
                        }
                    }
                    // Сохраняем смещения
                    final var offsets = this.incomeTopicsOffsetsController.getOffsetsByConfiguration(this.incomeTopicsConfiguration);
                    this.topicsOffsetsSaver.saveOffsets(TopicDirection.In, this.incomeTopicsConfiguration.getReaderName(), offsets);

                } catch (Exception e) {
                    internalTreatmentExceptionOnDataRead(event, e);
                }
            } finally {
                this.connectionsContainer.putCurrent(null);
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
    private void internalTreatmentExceptionOnDataRead(SimpleOnIterationExecuteEvent event, Exception e) {
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
            // TODO: Переделать в обработку Request-а и отправить изменение
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
            // TODO: Переделать в обработку Request-а и отправить изменение
            log.info("Securities: saved {} rows in {} ms", derivativeEntitiesPackage.size(), System.currentTimeMillis() - started);
        } finally {
            log.debug("Finished loadedDerivatives()");
        }
    }

    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Вспомогательные сервисы">
    public void publishSnapshot(@NotNull final String topic) throws Exception {
        final var descriptor = this.outcomeTopicsConfiguration.get(topic);
        // final var allObjects = descriptor.getD
        // this.outcomeTopicsUploader.publishFullSnapshot(descriptor, )
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
