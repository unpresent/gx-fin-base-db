package ru.gx.fin.base.db.descriptors;

import ru.gx.fin.base.db.dto.CurrenciesPackage;
import ru.gx.fin.base.db.dto.Currency;
import ru.gx.fin.base.db.entities.CurrencyEntity;
import ru.gx.kafka.upload.OutcomeTopicUploadingDescriptorsDefaults;
import ru.gx.std.upload.UploadingEntityDescriptor;

public class CurrenciesUploadingDescriptor extends UploadingEntityDescriptor<Currency, CurrenciesPackage, CurrencyEntity> {
    public CurrenciesUploadingDescriptor(String topic, OutcomeTopicUploadingDescriptorsDefaults defaults) {
        super(topic, defaults);
    }
}
