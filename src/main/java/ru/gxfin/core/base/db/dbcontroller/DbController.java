package ru.gxfin.core.base.db.dbcontroller;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import ru.gxfin.common.data.InvalidDataObjectTypeException;
import ru.gxfin.common.kafka.loader.PartitionOffset;
import ru.gxfin.common.kafka.loader.StandardIncomeTopicsLoader;
import ru.gxfin.common.worker.AbstractIterationExecuteEvent;
import ru.gxfin.common.worker.AbstractStartingExecuteEvent;
import ru.gxfin.common.worker.AbstractStoppingExecuteEvent;
import ru.gxfin.common.worker.AbstractWorker;
import ru.gxfin.core.base.db.converters.DerivativeEntityFromDtoConverter;
import ru.gxfin.core.base.db.converters.PlaceDtoFromEntityConverter;
import ru.gxfin.core.base.db.converters.SecurityEntityFromDtoConverter;
import ru.gxfin.core.base.db.entities.DerivativeEntitiesPackage;
import ru.gxfin.core.base.db.entities.SecurityEntitiesPackage;
import ru.gxfin.core.base.db.entities.kafka.KafkaIncomeOffsetEntity;
import ru.gxfin.core.base.db.entities.kafka.KafkaSnapshotPublishedOffsetEntity;
import ru.gxfin.core.base.db.events.*;
import ru.gxfin.core.base.db.repository.*;
import ru.gxfin.core.base.db.repository.kafka.KafkaOffsetsRepository;
import ru.gxfin.core.base.db.repository.kafka.KafkaSnapshotPublishedOffsetsRepository;
import ru.gxfin.core.base.db.utils.EntitiesUploader;
import ru.gxfin.core.base.db.memdata.InstrumentTypesMemoryRepository;
import ru.gxfin.core.base.db.memdata.PlacesMemoryRepository;
import ru.gxfin.core.base.db.memdata.ProviderTypesMemoryRepository;
import ru.gxfin.core.base.db.memdata.ProvidersMemoryRepository;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class DbController extends AbstractWorker {
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Fields">
    @Autowired
    private DbControllerSettingsController settings;

    @Autowired
    private StandardIncomeTopicsLoader incomeTopicsLoader;

    @Autowired
    private EntitiesUploader entitiesUploader;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private SecurityEntityFromDtoConverter securityEntityFromDtoConverter;

    @Autowired
    private DerivativeEntityFromDtoConverter derivativeEntityFromDtoConverter;

    @Autowired
    private KafkaOffsetsRepository kafkaOffsetsRepository;

    @Autowired
    private KafkaSnapshotPublishedOffsetsRepository kafkaSnapshotPublishedOffsetsRepository;

    private final Map<String, KafkaSnapshotPublishedOffsetEntity> kafkaSnapshotPublishedOffsetEntitiesCache = new HashMap<>();

    @Autowired
    private PlacesRepository placesRepository;

    @Autowired
    private PlacesMemoryRepository placesMemoryRepository;

    @Autowired
    private PlaceDtoFromEntityConverter placeDtoFromEntityConverter;

    @Autowired
    private ProviderTypesRepository providerTypesRepository;

    @Autowired
    private ProviderTypesMemoryRepository providerTypesMemoryRepository;

    @Autowired
    private ProvidersRepository providersRepository;

    @Autowired
    private ProvidersMemoryRepository providersMemoryRepository;

    @Autowired
    private InstrumentTypesRepository instrumentTypesRepository;

    @Autowired
    private InstrumentTypesMemoryRepository instrumentTypesMemoryRepository;

    @Autowired
    private SecuritiesRepository securitiesRepository;

    @Autowired
    private DerivativesRepository derivativesRepository;

    private SessionFactory sessionFactory;

    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Settings">
    @EventListener
    public void onEventChangedSettings(DbControllerSettingsChangedEvent event) {
        log.info("onEventChangedSettings({})", event.getSettingName());
    }

    @Override
    protected int getMinTimePerIterationMs() {
        return this.settings.getMinTimePerIterationMs();
    }

    @Override
    protected int getTimoutRunnerLifeMs() {
        return this.settings.getTimeoutLifeMs();
    }

    @Override
    public int getWaitOnStopMs() {
        return this.settings.getWaitOnStopMs();
    }

    @Override
    public int getWaitOnRestartMs() {
        return this.settings.getWaitOnRestartMs();
    }

    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Инициализация">
    public DbController(String serviceName) {
        super(serviceName);
    }

    @Override
    protected AbstractIterationExecuteEvent createIterationExecuteEvent() {
        return new DbControllerIterationExecuteEvent(this);
    }

    @Override
    protected AbstractStartingExecuteEvent createStartingExecuteEvent() {
        return new DbControllerStartingExecuteEvent(this);
    }

    @Override
    protected AbstractStoppingExecuteEvent createStoppingExecuteEvent() {
        return new DbControllerStoppingExecuteEvent(this);
    }
    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Обработка событий Worker-а">

    /**
     * Обработка события о начале работы цикла итераций.
     *
     * @param event Объект-событие с параметрами.
     */
    @SuppressWarnings("unused")
    @EventListener(DbControllerStartingExecuteEvent.class)
    public void startingExecute(DbControllerStartingExecuteEvent event) throws Exception {
        log.info("Starting startingExecute()");
        if (this.sessionFactory == null) {
            if (entityManagerFactory.unwrap(SessionFactory.class) == null) {
                throw new NullPointerException("entityManagerFactory is not a hibernate factory!");
            }
            this.sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        }

        internalSeekIncomeOffsetsOnStart();

        internalLoadMemoryRepositoriesOnStart();

        log.info("Finished startingExecute()");
    }

    protected void internalSeekIncomeOffsetsOnStart() {
        final var offsets = this.kafkaOffsetsRepository.findAll();

        var partitionsOffset = new ArrayList<PartitionOffset>();
        for (var topicDescriptor : this.incomeTopicsLoader.getAll()) {
            partitionsOffset.clear();
            offsets.forEach(o -> {
                if (o.getTopic() != null && o.getTopic().equals(topicDescriptor.getTopic())) {
                    partitionsOffset.add(new PartitionOffset(o.getPartition(), o.getOffset()));
                }
            });
            if (partitionsOffset.size() <= 0) {
                log.info("Topic: {}. Seek to begin all partitions in topic", topicDescriptor.getTopic());
                this.incomeTopicsLoader.seekTopicAllPartitionsToBegin(topicDescriptor.getTopic());
            } else {
                log.info("Topic: {}. Seek to {}", partitionsOffset.stream().map(po -> po.getPartition() + ":" + po.getOffset()).collect(Collectors.joining(",")), topicDescriptor.getTopic());
                this.incomeTopicsLoader.seekTopic(topicDescriptor.getTopic(), partitionsOffset);
            }
        }
    }

    protected void internalLoadMemoryRepositoriesOnStart() throws Exception {
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
    @EventListener(DbControllerStoppingExecuteEvent.class)
    public void stoppingExecute(DbControllerStoppingExecuteEvent event) {
        log.debug("Starting stoppingExecute()");
        log.debug("Finished stoppingExecute()");
    }

    /**
     * Обработчик итераций.
     *
     * @param event Объект-событие с параметрами итерации.
     */
    @EventListener(DbControllerIterationExecuteEvent.class)
    public void iterationExecute(DbControllerIterationExecuteEvent event) {
        log.debug("Starting iterationExecute()");
        try {
            runnerIsLifeSet();
            event.setImmediateRunNextIteration(false);

            final var session = this.sessionFactory.openSession();
            try (session) {
                final var tran = session.beginTransaction();
                try {
                    final var durationOnPoll = this.settings.getDurationOnPollMs();
                    this.incomeTopicsLoader.processAllTopics(durationOnPoll);

                    saveKafkaOffsets();

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
    private void internalTreatmentExceptionOnDataRead(DbControllerIterationExecuteEvent event, Exception e) {
        log.error("", e);
        if (e instanceof InterruptedException) {
            log.info("event.setStopExecution(true)");
            event.setStopExecution(true);
        } else {
            log.info("event.setNeedRestart(true)");
            event.setNeedRestart(true);
        }
    }

    /**
     * Сохранение в БД текущих смещений Kafka.
     */
    private void saveKafkaOffsets() {
        final var kafkaOffsets = new ArrayList<KafkaIncomeOffsetEntity>();
        final var pCount = this.incomeTopicsLoader.prioritiesCount();
        for (int p = 0; p < pCount; p++) {
            this.incomeTopicsLoader
                    .getByPriority(p)
                    .forEach(topicDescriptor ->
                            topicDescriptor.getPartitions().forEach(partition -> {
                                final var offset = new KafkaIncomeOffsetEntity()
                                        .setReader(this.getName())
                                        .setTopic(topicDescriptor.getTopic())
                                        .setPartition(partition)
                                        .setOffset(topicDescriptor.getOffset(partition));
                                kafkaOffsets.add(offset);
                            })
                    );
        }

        final var started = System.currentTimeMillis();
        this.kafkaOffsetsRepository.saveAllAndFlush(kafkaOffsets);
        log.info("KafkaOffsets: saved {} rows in {} ms", kafkaOffsets.size(), System.currentTimeMillis() - started);
    }
    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Обработка событий о чтении данных из Kafka">

    /**
     * Обработка события о загрузке из Kafka набора объектов {@link ru.gxfin.core.base.db.dto.Security}.
     *
     * @param event Объект-событие с параметрами.
     */
    @EventListener(LoadedSecuritiesEvent.class)
    public void loadedSecurities(LoadedSecuritiesEvent event) throws InvalidDataObjectTypeException {
        log.debug("Starting loadedSecurities()");
        try {
            runnerIsLifeSet();

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
     * Обработка события о загрузке из Kafka набора объектов {@link ru.gxfin.core.base.db.dto.Derivative}.
     *
     * @param event Объект-событие с параметрами.
     */
    @EventListener(LoadedDerivativesEvent.class)
    public void loadedDerivatives(LoadedDerivativesEvent event) throws InvalidDataObjectTypeException {
        log.debug("Starting loadedDerivatives()");
        try {
            runnerIsLifeSet();

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
    private void reloadKafkaSnapshotPublishedOffsetEntities() {
        final var kafkaSnapshotPublishedOffsetEntities = this.kafkaSnapshotPublishedOffsetsRepository.findAll();
        synchronized (this) {
            this.kafkaSnapshotPublishedOffsetEntitiesCache.clear();
            kafkaSnapshotPublishedOffsetEntities.forEach(offsetEntity -> this.kafkaSnapshotPublishedOffsetEntitiesCache.put(offsetEntity.getTopic(), offsetEntity));
        }
    }

    public void publishSnapshot(@NotNull String topic) throws Exception {
        final var descriptor = this.entitiesUploader.getDescriptor(topic);
        log.info("Starting loading dictionary {}", descriptor.getEntityClass().getSimpleName());
        final var allObjects = descriptor.getRepository().findAll();
        log.info("Starting publish dictionary {} into topic {}; object count {}", descriptor.getEntityClass().getSimpleName(), descriptor.getTopic(), allObjects.size());
        final var partitionOffset = this.entitiesUploader.uploadSnapshot(descriptor.getEntityClass(), allObjects);

        final var kafkaSnapshotPublishedOffsetEntity = new KafkaSnapshotPublishedOffsetEntity()
                .setTopic(descriptor.getTopic())
                .setPartition(partitionOffset.getPartition())
                .setStartOffset(partitionOffset.getOffset());
        this.kafkaSnapshotPublishedOffsetEntitiesCache.put(descriptor.getTopic(), kafkaSnapshotPublishedOffsetEntity);
        this.kafkaSnapshotPublishedOffsetsRepository.save(kafkaSnapshotPublishedOffsetEntity);
        log.info("Dictionary {} published into topic {}. Partition: {}, offset: {}", descriptor.getEntityClass().getSimpleName(), descriptor.getTopic(), partitionOffset.getPartition(), partitionOffset.getOffset());
    }

    public KafkaSnapshotPublishedOffsetEntity getOffset(String topic) {
        return this.kafkaSnapshotPublishedOffsetEntitiesCache.get(topic);
    }
    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
}
