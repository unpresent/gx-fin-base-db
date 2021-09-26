package ru.gxfin.core.base.db.utils;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import ru.gxfin.common.data.*;
import ru.gxfin.common.kafka.loader.PartitionOffset;
import ru.gxfin.common.kafka.uploader.StandardOutcomeTopicUploader;

import java.util.*;

@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ToString
public class EntitiesUploader {
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Fields">
    @Getter(AccessLevel.PROTECTED)
    private final Map<Class<? extends EntityObject>, Collection<EntityObject>> changesMap;

    private final List<UploadingEntityDescriptor<? extends DataObject, ? extends DataPackage<DataObject>, ? extends EntityObject>> uploadingEntityDescriptors;

    @Getter
    protected final StandardOutcomeTopicUploader standardOutcomeTopicUploader;

    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Initialization">
    public EntitiesUploader(@NotNull ObjectMapper objectMapper) {
        this.standardOutcomeTopicUploader = new StandardOutcomeTopicUploader(objectMapper);
        this.changesMap = new HashMap<>();
        this.uploadingEntityDescriptors = new ArrayList<>();
    }
    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="API">

    /**
     * Если описателя с таким топиком еще нет, то добавляется. Если есть, то заменяется.
     *
     * @param descriptor Описатель, который надо зарегистрировать.
     * @return this.
     */
    public <O extends DataObject, P extends DataPackage<O>, E extends EntityObject> EntitiesUploader register(@NotNull UploadingEntityDescriptor<O, P, E> descriptor) {
        final var descriptors = this.uploadingEntityDescriptors;
        for (int i = 0; i < descriptors.size(); i++) {
            final var local = descriptors.get(i);
            if (local.getTopic().equals(descriptor.getTopic())) {
                descriptors.set(i, (UploadingEntityDescriptor<? extends DataObject, ? extends DataPackage<DataObject>, ? extends EntityObject>) descriptor);
                return this;
            }
        }
        descriptors.add((UploadingEntityDescriptor<? extends DataObject, ? extends DataPackage<DataObject>, ? extends EntityObject>) descriptor);
        return this;
    }

    /**
     * @param topic Топик, для которого требуется найти описатель выгрузки.
     * @return Получение описателя с указанным топиком. Если такого описателя нет, то возвращается null.
     */
    @SuppressWarnings("unchecked")
    public <O extends DataObject, P extends DataPackage<O>, E extends EntityObject> UploadingEntityDescriptor<O, P, E> getDescriptor(@NotNull String topic) {
        for (var descriptor : getAllDescriptors()) {
            if (descriptor.getTopic().equals(topic)) {
                return (UploadingEntityDescriptor<O, P, E>) descriptor;
            }
        }
        return null;
    }

    /**
     * @param entityClass Класс сущности, для которого требуется найти описатель выгрузки.
     * @return Получение описателя с указанным классом сущности. Если такого описателя нет, то возвращается null.
     */
    @SuppressWarnings("unchecked")
    public <O extends DataObject, P extends DataPackage<O>, E extends EntityObject> UploadingEntityDescriptor<O, P, E> getDescriptor(@NotNull Class<? extends EntityObject> entityClass) {
        for (var descriptor : getAllDescriptors()) {
            if (descriptor.getEntityClass() == entityClass) {
                return (UploadingEntityDescriptor<O, P, E>) descriptor;
            }
        }
        return null;
    }

    /**
     * @return Список всех описателей выгрузки.
     */
    public Collection<UploadingEntityDescriptor<? extends DataObject, ? extends DataPackage<DataObject>, ? extends EntityObject>> getAllDescriptors() {
        return this.uploadingEntityDescriptors;
    }

    protected Collection<EntityObject> getChangesList(@NotNull Class<? extends EntityObject> entityClass) {
        return this.changesMap.computeIfAbsent(entityClass, k -> new ArrayList<>());
    }

    /**
     * Фиксация изменений у сущностей указанного класса.
     *
     * @param entityClass     Класс сущности изменившихся экземпляров.
     * @param changedEntities Список изменившихся экземпляров сущности.
     */
    public void setChanges(Class<? extends EntityObject> entityClass, Collection<EntityObject> changedEntities) {
        synchronized (entityClass) {
            final var list = getChangesList(entityClass);
            changedEntities.forEach(e -> {
                if (!list.contains(e)) {
                    list.add(e);
                }
            });
        }
    }

    public PartitionOffset uploadChangesByEntityClass(@NotNull Class<? extends EntityObject> entityClass) throws Exception {
        synchronized (entityClass) {
            // Получаем описатель выгрузки для данной сущности
            final var uploadDescriptor = getDescriptor(entityClass);
            // Получаем список изменений
            var entityObjects = getChangesList(entityClass);
            // Конвертируем список Entity -> DTO-пакет
            final var dtoPackage = this.standardOutcomeTopicUploader.createPackage(uploadDescriptor);
            uploadDescriptor.getConverter().fillDtoPackageFromEntitiesPackage(dtoPackage, entityObjects);
            // Выгружаем в Kafka
            final var result = this.standardOutcomeTopicUploader.uploadDataPackage(uploadDescriptor, dtoPackage);
            // Чистим список изменений
            entityObjects.clear();
            return result;
        }
    }

    public PartitionOffset uploadSnapshot(@NotNull Class<? extends EntityObject> entityClass, @NotNull Collection<EntityObject> allObjects) throws Exception {
        synchronized (entityClass) {
            // Получаем описатель выгрузки для данной сущности
            final var uploadDescriptor = getDescriptor(entityClass);
            // Конвертируем список Entity -> DTO-пакет
            final var dtoPackage = createAndFillDtoPackage(uploadDescriptor, allObjects);
            // Обновляем memoryRepository
            internalLoadToMemoryRepository(uploadDescriptor, dtoPackage);
            // Выгружаем в Kafka
            final var result = this.standardOutcomeTopicUploader.uploadDataPackage(uploadDescriptor, dtoPackage);
            // Чистим список изменений
            getChangesList(entityClass).clear();
            return result;
        }
    }
    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Internal methods">
    protected <O extends DataObject, P extends DataPackage<O>, E extends EntityObject> P createAndFillDtoPackage(
            @NotNull UploadingEntityDescriptor<O, P, E> uploadDescriptor,
            @NotNull Collection<E> allObjects
    ) throws Exception {
        final var dtoPackage = this.standardOutcomeTopicUploader.createPackage(uploadDescriptor);
        uploadDescriptor.getConverter().fillDtoPackageFromEntitiesPackage(dtoPackage, allObjects);
        return dtoPackage;
    }

    protected <O extends DataObject, P extends DataPackage<O>, E extends EntityObject> void internalLoadToMemoryRepository(
            @NotNull UploadingEntityDescriptor<O, P, E> uploadDescriptor,
            @NotNull P dtoPackage
    ) throws JsonMappingException, ObjectNotExistsException, ObjectAlreadyExistsException {
        final var memRepo = uploadDescriptor.getMemoryRepository();
        if (memRepo == null) {
            return;
        }

        for(var dataObject: dtoPackage.getObjects()) {
            final var key = memRepo.extractKey(dataObject);
            if (memRepo.containsKey(key)) {
                memRepo.update(dataObject);
            } else {
                memRepo.insert(dataObject);
            }
        }
    }
    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
}
