package ru.gx.fin.base.db.descriptors;

import org.jetbrains.annotations.NotNull;
import ru.gx.fin.base.db.dto.Derivative;
import ru.gx.fin.base.db.dto.DerivativesPackage;
import ru.gx.kafka.load.IncomeTopicLoadingDescriptor;
import ru.gx.kafka.load.IncomeTopicLoadingDescriptorsDefaults;

public class DerivativesLoadingDescriptor extends IncomeTopicLoadingDescriptor<Derivative, DerivativesPackage> {
    public DerivativesLoadingDescriptor(@NotNull String topic, IncomeTopicLoadingDescriptorsDefaults defaults) {
        super(topic, defaults);
    }
}
