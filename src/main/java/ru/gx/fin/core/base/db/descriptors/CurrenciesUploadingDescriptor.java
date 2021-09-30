package ru.gx.fin.core.base.db.descriptors;

import ru.gx.fin.core.base.db.dto.CurrenciesPackage;
import ru.gx.fin.core.base.db.dto.Currency;
import ru.gx.fin.core.base.db.entities.CurrencyEntity;
import ru.gx.kafka.upload.OutcomeTopicUploadingDescriptorsDefaults;
import ru.gx.kafka.upload.UploadingEntityDescriptor;

public class CurrenciesUploadingDescriptor extends UploadingEntityDescriptor<Currency, CurrenciesPackage, CurrencyEntity> {
    public CurrenciesUploadingDescriptor(String topic, OutcomeTopicUploadingDescriptorsDefaults defaults) {
        super(topic, defaults);
    }
}
