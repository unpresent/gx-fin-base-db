package ru.gx.fin.base.db.descriptors;

import ru.gx.fin.base.db.dto.ProviderType;
import ru.gx.fin.base.db.dto.ProviderTypesPackage;
import ru.gx.fin.base.db.entities.ProviderTypeEntity;
import ru.gx.kafka.upload.OutcomeTopicUploadingDescriptorsDefaults;
import ru.gx.std.upload.UploadingEntityDescriptor;

public class ProviderTypesUploadingDescriptor extends UploadingEntityDescriptor<ProviderType, ProviderTypesPackage, ProviderTypeEntity> {
    public ProviderTypesUploadingDescriptor(String topic, OutcomeTopicUploadingDescriptorsDefaults defaults) {
        super(topic, defaults);
    }
}
