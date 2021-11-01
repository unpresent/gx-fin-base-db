package ru.gx.fin.base.db.descriptors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.gx.fin.base.db.dto.Derivative;
import ru.gx.fin.base.db.dto.DerivativesPackage;
import ru.gx.kafka.load.AbstractIncomeTopicsConfiguration;
import ru.gx.kafka.load.IncomeTopicLoadingDescriptorsDefaults;
import ru.gx.kafka.load.StandardIncomeTopicLoadingDescriptor;

public class DerivativesLoadingDescriptor extends StandardIncomeTopicLoadingDescriptor<Derivative, DerivativesPackage> {
    public DerivativesLoadingDescriptor(@NotNull final AbstractIncomeTopicsConfiguration owner, @NotNull final String topic, @Nullable final IncomeTopicLoadingDescriptorsDefaults defaults) {
        super(owner, topic, defaults);
    }
}
