package ru.gxfin.core.base.db.descriptors;

import ru.gxfin.common.kafka.uploader.OutcomeTopicUploadingDescriptorsDefaults;
import ru.gxfin.core.base.db.entities.ProviderTypeEntity;
import ru.gxfin.core.base.db.utils.UploadingEntityDescriptor;
import ru.gxfin.core.base.db.dto.ProviderType;
import ru.gxfin.core.base.db.dto.ProviderTypesPackage;

public class ProviderTypesUploadingDescriptor extends UploadingEntityDescriptor<ProviderType, ProviderTypesPackage, ProviderTypeEntity> {
    public ProviderTypesUploadingDescriptor(String topic, OutcomeTopicUploadingDescriptorsDefaults defaults) {
        super(topic, defaults);
    }
}
