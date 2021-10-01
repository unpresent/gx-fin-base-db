package ru.gx.fin.base.db.descriptors;

import ru.gx.fin.base.db.dto.InstrumentTypesPackage;
import ru.gx.fin.base.db.entities.InstrumentTypeEntity;
import ru.gx.fin.base.db.dto.InstrumentType;
import ru.gx.kafka.upload.OutcomeTopicUploadingDescriptorsDefaults;
import ru.gx.std.upload.UploadingEntityDescriptor;

public class InstrumentTypesUploadingDescriptor extends UploadingEntityDescriptor<InstrumentType, InstrumentTypesPackage, InstrumentTypeEntity> {
    public InstrumentTypesUploadingDescriptor(String topic, OutcomeTopicUploadingDescriptorsDefaults defaults) {
        super(topic, defaults);
    }
}
