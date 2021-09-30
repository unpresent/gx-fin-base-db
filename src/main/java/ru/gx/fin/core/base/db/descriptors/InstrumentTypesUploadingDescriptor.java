package ru.gx.fin.core.base.db.descriptors;

import ru.gx.fin.core.base.db.dto.InstrumentType;
import ru.gx.fin.core.base.db.dto.InstrumentTypesPackage;
import ru.gx.fin.core.base.db.entities.InstrumentTypeEntity;
import ru.gx.kafka.upload.OutcomeTopicUploadingDescriptorsDefaults;
import ru.gx.kafka.upload.UploadingEntityDescriptor;

public class InstrumentTypesUploadingDescriptor extends UploadingEntityDescriptor<InstrumentType, InstrumentTypesPackage, InstrumentTypeEntity> {
    public InstrumentTypesUploadingDescriptor(String topic, OutcomeTopicUploadingDescriptorsDefaults defaults) {
        super(topic, defaults);
    }
}
