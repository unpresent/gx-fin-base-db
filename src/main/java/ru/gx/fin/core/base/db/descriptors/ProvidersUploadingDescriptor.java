package ru.gx.fin.core.base.db.descriptors;

import ru.gx.fin.core.base.db.dto.Provider;
import ru.gx.fin.core.base.db.dto.ProvidersPackage;
import ru.gx.fin.core.base.db.entities.ProviderEntity;
import ru.gx.kafka.upload.OutcomeTopicUploadingDescriptorsDefaults;
import ru.gx.kafka.upload.UploadingEntityDescriptor;

public class ProvidersUploadingDescriptor extends UploadingEntityDescriptor<Provider, ProvidersPackage, ProviderEntity> {
    public ProvidersUploadingDescriptor(String topic, OutcomeTopicUploadingDescriptorsDefaults defaults) {
        super(topic, defaults);
    }
}
