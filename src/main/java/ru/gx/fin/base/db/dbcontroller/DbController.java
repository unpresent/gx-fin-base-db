package ru.gx.fin.base.db.dbcontroller;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.repository.CrudRepository;
import ru.gx.data.*;
import ru.gx.data.edlinking.DtoFromEntityConverter;
import ru.gx.data.edlinking.EntitiesDtoLinksConfigurationException;
import ru.gx.data.edlinking.EntitiesDtosLinksConfiguration;
import ru.gx.data.entity.EntityObject;
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
import ru.gx.kafka.load.IncomeTopicsOffsetsController;
import ru.gx.kafka.load.SimpleIncomeTopicsConfiguration;
import ru.gx.kafka.load.StandardIncomeTopicsLoader;
import ru.gx.kafka.offsets.PartitionOffset;
import ru.gx.kafka.offsets.TopicsOffsetsLoader;
import ru.gx.kafka.offsets.TopicsOffsetsSaver;
import ru.gx.kafka.upload.SimpleOutcomeTopicsConfiguration;
import ru.gx.kafka.upload.StandardOutcomeTopicsUploader;
import ru.gx.worker.SimpleOnIterationExecuteEvent;
import ru.gx.worker.SimpleOnStartingExecuteEvent;
import ru.gx.worker.SimpleOnStoppingExecuteEvent;
import ru.gx.worker.SimpleWorker;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import java.util.Collection;

import static lombok.AccessLevel.PROTECTED;

@Slf4j
public class DbController {
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Fields">
    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private DataSource dataSource;

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

    @Getter
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private EntitiesDtosLinksConfiguration entitiesDtosLinksConfiguration;

    @Getter
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private EntityManagerFactory entityManagerFactory;

    @Getter
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private ActiveSessionsContainer activeSessionsContainer;

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

        if (this.sessionFactory == null) {
            if (entityManagerFactory.unwrap(SessionFactory.class) == null) {
                throw new NullPointerException("entityManagerFactory is not a hibernate factory!");
            }
            this.sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        }

        try (var session = this.sessionFactory.openSession()) {
            this.activeSessionsContainer.putCurrent(session);
            final var offsets = this.topicsOffsetsLoader.loadOffsets(TopicDirection.In, this.incomeTopicsConfiguration.getReaderName());
            if (offsets.size() <= 0) {
                this.incomeTopicsOffsetsController.seekAllToBegin(this.incomeTopicsConfiguration);
            } else {
                this.incomeTopicsOffsetsController.seekTopicsByList(this.incomeTopicsConfiguration, offsets);
            }
        } finally {
            this.activeSessionsContainer.putCurrent(null);
        }

        publishAllOnStart();

        log.debug("Finished startingExecute()");

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

            final var session = this.sessionFactory.openSession();
            try (session) {
                this.activeSessionsContainer.putCurrent(session);
                final var tran = session.beginTransaction();

                try {
                    // Загружаем данные и сохраняем в БД
                    final var result = this.incomeTopicsLoader.processAllTopics(this.incomeTopicsConfiguration);
                    for (var c: result.values()) {
                        if (c.size() > 1) {
                            event.setImmediateRunNextIteration(true);
                            break;
                        }
                    }

                    // Сохраняем смещения
                    final var offsets = this.incomeTopicsOffsetsController.getOffsetsByConfiguration(this.incomeTopicsConfiguration);
                    this.topicsOffsetsSaver.saveOffsets(TopicDirection.In, this.incomeTopicsConfiguration.getReaderName(), offsets);

                    tran.commit();
                } catch (Exception e) {
                    tran.rollback();
                    internalTreatmentExceptionOnDataRead(event, e);
                }
            } finally {
                this.activeSessionsContainer.putCurrent(null);
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
    protected void publishAllOnStart() throws Exception {
        for (var c : this.outcomeTopicsConfiguration.getAll()) {
            publishSnapshot(c.getTopic());
        }
    }

    @SuppressWarnings("unchecked")
    public void publishSnapshot(@NotNull final String topic) throws Exception {
        final var topicDescriptor = this
                .outcomeTopicsConfiguration
                .get(topic);

        final var linkDescriptor = this
                .entitiesDtosLinksConfiguration
                .getByDtoClass(topicDescriptor.getDataObjectClass());

        final var repository = linkDescriptor.getRepository();
        if (repository == null) {
            throw new EntitiesDtoLinksConfigurationException("Can't get CrudRepository by dtoClass " + topicDescriptor.getDataObjectClass().getName());
        }

        final var converter = linkDescriptor.getDtoFromEntityConverter();
        if (converter == null) {
            throw new EntitiesDtoLinksConfigurationException("Can't get Converter by dtoClass " + topicDescriptor.getDataObjectClass().getName());
        }

        // Загружаем данные из БД:
        final var entityObjects = repository.findAll();

        // Преобразуем в DTO
        final var dataPackage = linkDescriptor.createDtoPackage();
        converter.fillDtoPackageFromEntitiesPackage(dataPackage, entityObjects);
        final var dataObjects = (Collection<DataObject>)dataPackage.getObjects();

        // Выгружаем данные
        this.outcomeTopicsUploader.publishFullSnapshot(topicDescriptor, dataObjects, null);
    }

    @NotNull
    public PartitionOffset getLastPublishedSnapshotOffset(@NotNull final String topic) {
        final var topicDescriptor = this
                .outcomeTopicsConfiguration
                .get(topic);

        var partitionOffset = this.outcomeTopicsUploader
                .getLastPublishedSnapshotOffset(topicDescriptor);

        if (partitionOffset == null) {
            partitionOffset =new PartitionOffset(0, 0);
        }

        return partitionOffset;
    }
    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
}
