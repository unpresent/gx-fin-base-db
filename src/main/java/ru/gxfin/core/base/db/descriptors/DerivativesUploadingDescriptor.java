package ru.gxfin.core.base.db.descriptors;

import ru.gxfin.common.kafka.uploader.OutcomeTopicUploadingDescriptorsDefaults;
import ru.gxfin.core.base.db.entities.DerivativeEntity;
import ru.gxfin.core.base.db.utils.UploadingEntityDescriptor;
import ru.gxfin.core.base.db.dto.Derivative;
import ru.gxfin.core.base.db.dto.DerivativesPackage;

public class DerivativesUploadingDescriptor extends UploadingEntityDescriptor<Derivative, DerivativesPackage, DerivativeEntity> {
    public DerivativesUploadingDescriptor(String topic, OutcomeTopicUploadingDescriptorsDefaults defaults) {
        super(topic, defaults);
    }
}
