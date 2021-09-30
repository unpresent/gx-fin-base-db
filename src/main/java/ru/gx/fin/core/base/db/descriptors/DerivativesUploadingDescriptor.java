package ru.gx.fin.core.base.db.descriptors;

import ru.gx.fin.core.base.db.dto.Derivative;
import ru.gx.fin.core.base.db.dto.DerivativesPackage;
import ru.gx.fin.core.base.db.entities.DerivativeEntity;
import ru.gx.kafka.upload.OutcomeTopicUploadingDescriptorsDefaults;
import ru.gx.kafka.upload.UploadingEntityDescriptor;

public class DerivativesUploadingDescriptor extends UploadingEntityDescriptor<Derivative, DerivativesPackage, DerivativeEntity> {
    public DerivativesUploadingDescriptor(String topic, OutcomeTopicUploadingDescriptorsDefaults defaults) {
        super(topic, defaults);
    }
}
