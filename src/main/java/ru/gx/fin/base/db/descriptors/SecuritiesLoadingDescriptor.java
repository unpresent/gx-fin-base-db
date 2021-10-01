package ru.gx.fin.base.db.descriptors;

import org.jetbrains.annotations.NotNull;
import ru.gx.kafka.load.IncomeTopicLoadingDescriptor;
import ru.gx.kafka.load.IncomeTopicLoadingDescriptorsDefaults;
import ru.gx.fin.base.db.dto.SecuritiesPackage;
import ru.gx.fin.base.db.dto.Security;

public class SecuritiesLoadingDescriptor extends IncomeTopicLoadingDescriptor<Security, SecuritiesPackage> {
    public SecuritiesLoadingDescriptor(@NotNull String topic, IncomeTopicLoadingDescriptorsDefaults defaults) {
        super(topic, defaults);
    }
}
