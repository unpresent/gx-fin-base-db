package ru.gxfin.core.base.db.descriptors;

import ru.gxfin.common.kafka.uploader.OutcomeTopicUploadingDescriptorsDefaults;
import ru.gxfin.core.base.db.entities.CurrencyEntity;
import ru.gxfin.core.base.db.utils.UploadingEntityDescriptor;
import ru.gxfin.core.base.db.dto.CurrenciesPackage;
import ru.gxfin.core.base.db.dto.Currency;

public class CurrenciesUploadingDescriptor extends UploadingEntityDescriptor<Currency, CurrenciesPackage, CurrencyEntity> {
    public CurrenciesUploadingDescriptor(String topic, OutcomeTopicUploadingDescriptorsDefaults defaults) {
        super(topic, defaults);
    }
}
