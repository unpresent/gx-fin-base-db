package ru.gxfin.core.base.db.utils;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.gxfin.common.data.*;
import ru.gxfin.common.kafka.uploader.OutcomeTopicUploadingDescriptor;
import ru.gxfin.common.kafka.uploader.OutcomeTopicUploadingDescriptorsDefaults;

import java.lang.reflect.ParameterizedType;

@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString
public class UploadingEntityDescriptor<O extends DataObject, P extends DataPackage<O>, E extends EntityObject>
        extends OutcomeTopicUploadingDescriptor<O, P> {

    @Getter
    @Setter
    private Class<? extends EntityObject> entityClass;

    @Getter
    @Setter
    private AbstractDtoFromEntityConverter<O, P, E> converter;

    @Getter
    @Setter
    private JpaRepository<E, ?> repository;

    @Getter
    @Setter
    private DataMemoryRepository<O, P> memoryRepository;

    //    @SuppressWarnings("unchecked")
    //    public <OR extends DataObject, PR extends DataPackage<OR>>
    //    UploadingEntityDescriptor<O, P, E> setRootMemoryRepository(DataMemoryRepository<OR, PR> memoryRepository) {
    //        this.memoryRepository = (DataMemoryRepository<O, P>) memoryRepository;
    //        return this;
    //    }

    @SuppressWarnings("unchecked")
    public UploadingEntityDescriptor(String topic, OutcomeTopicUploadingDescriptorsDefaults defaults) {
        super(topic, defaults);

        final var thisClass = this.getClass();
        final var superClass = thisClass.getGenericSuperclass();
        if (superClass instanceof ParameterizedType) {
            this.entityClass = (Class<E>) ((ParameterizedType) superClass).getActualTypeArguments()[2];
        }
    }
}
