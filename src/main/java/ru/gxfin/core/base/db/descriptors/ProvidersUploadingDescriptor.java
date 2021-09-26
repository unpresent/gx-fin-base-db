package ru.gxfin.core.base.db.descriptors;

import ru.gxfin.common.kafka.uploader.OutcomeTopicUploadingDescriptorsDefaults;
import ru.gxfin.core.base.db.entities.ProviderEntity;
import ru.gxfin.core.base.db.utils.UploadingEntityDescriptor;
import ru.gxfin.core.base.db.dto.Provider;
import ru.gxfin.core.base.db.dto.ProvidersPackage;

public class ProvidersUploadingDescriptor extends UploadingEntityDescriptor<Provider, ProvidersPackage, ProviderEntity> {
    public ProvidersUploadingDescriptor(String topic, OutcomeTopicUploadingDescriptorsDefaults defaults) {
        super(topic, defaults);
    }
}
