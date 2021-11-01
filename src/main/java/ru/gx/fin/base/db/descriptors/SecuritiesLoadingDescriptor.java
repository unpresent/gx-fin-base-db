package ru.gx.fin.base.db.descriptors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.gx.kafka.load.AbstractIncomeTopicsConfiguration;
import ru.gx.kafka.load.IncomeTopicLoadingDescriptor;
import ru.gx.kafka.load.IncomeTopicLoadingDescriptorsDefaults;
import ru.gx.fin.base.db.dto.SecuritiesPackage;
import ru.gx.fin.base.db.dto.Security;
import ru.gx.kafka.load.StandardIncomeTopicLoadingDescriptor;

public class SecuritiesLoadingDescriptor extends StandardIncomeTopicLoadingDescriptor<Security, SecuritiesPackage> {
    public SecuritiesLoadingDescriptor(@NotNull final AbstractIncomeTopicsConfiguration owner, @NotNull final String topic, @Nullable final IncomeTopicLoadingDescriptorsDefaults defaults) {
        super(owner, topic, defaults);
    }
}
