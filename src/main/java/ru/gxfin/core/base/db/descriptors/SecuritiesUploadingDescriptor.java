package ru.gxfin.core.base.db.descriptors;

import ru.gxfin.common.kafka.uploader.OutcomeTopicUploadingDescriptorsDefaults;
import ru.gxfin.core.base.db.entities.SecurityEntity;
import ru.gxfin.core.base.db.utils.UploadingEntityDescriptor;
import ru.gxfin.core.base.db.dto.SecuritiesPackage;
import ru.gxfin.core.base.db.dto.Security;

public class SecuritiesUploadingDescriptor extends UploadingEntityDescriptor<Security, SecuritiesPackage, SecurityEntity> {
    public SecuritiesUploadingDescriptor(String topic, OutcomeTopicUploadingDescriptorsDefaults defaults) {
        super(topic, defaults);
    }
}
