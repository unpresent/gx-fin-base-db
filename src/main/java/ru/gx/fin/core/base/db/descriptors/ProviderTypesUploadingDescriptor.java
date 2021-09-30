package ru.gx.fin.core.base.db.descriptors;

import ru.gx.fin.core.base.db.dto.ProviderType;
import ru.gx.fin.core.base.db.dto.ProviderTypesPackage;
import ru.gx.fin.core.base.db.entities.ProviderTypeEntity;
import ru.gx.kafka.upload.OutcomeTopicUploadingDescriptorsDefaults;
import ru.gx.kafka.upload.UploadingEntityDescriptor;

public class ProviderTypesUploadingDescriptor extends UploadingEntityDescriptor<ProviderType, ProviderTypesPackage, ProviderTypeEntity> {
    public ProviderTypesUploadingDescriptor(String topic, OutcomeTopicUploadingDescriptorsDefaults defaults) {
        super(topic, defaults);
    }
}
