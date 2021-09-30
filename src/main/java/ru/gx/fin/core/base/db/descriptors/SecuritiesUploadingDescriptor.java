package ru.gx.fin.core.base.db.descriptors;

import ru.gx.fin.core.base.db.dto.SecuritiesPackage;
import ru.gx.fin.core.base.db.dto.Security;
import ru.gx.fin.core.base.db.entities.SecurityEntity;
import ru.gx.kafka.upload.OutcomeTopicUploadingDescriptorsDefaults;
import ru.gx.kafka.upload.UploadingEntityDescriptor;

public class SecuritiesUploadingDescriptor extends UploadingEntityDescriptor<Security, SecuritiesPackage, SecurityEntity> {
    public SecuritiesUploadingDescriptor(String topic, OutcomeTopicUploadingDescriptorsDefaults defaults) {
        super(topic, defaults);
    }
}
