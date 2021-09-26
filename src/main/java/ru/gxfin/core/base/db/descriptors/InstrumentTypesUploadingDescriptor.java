package ru.gxfin.core.base.db.descriptors;

import ru.gxfin.common.kafka.uploader.OutcomeTopicUploadingDescriptorsDefaults;
import ru.gxfin.core.base.db.entities.InstrumentTypeEntity;
import ru.gxfin.core.base.db.utils.UploadingEntityDescriptor;
import ru.gxfin.core.base.db.dto.InstrumentType;
import ru.gxfin.core.base.db.dto.InstrumentTypesPackage;

public class InstrumentTypesUploadingDescriptor extends UploadingEntityDescriptor<InstrumentType, InstrumentTypesPackage, InstrumentTypeEntity> {
    public InstrumentTypesUploadingDescriptor(String topic, OutcomeTopicUploadingDescriptorsDefaults defaults) {
        super(topic, defaults);
    }
}
