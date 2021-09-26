package ru.gxfin.core.base.db.descriptors;

import org.jetbrains.annotations.NotNull;
import ru.gxfin.common.kafka.loader.IncomeTopicLoadingDescriptor;
import ru.gxfin.common.kafka.loader.IncomeTopicLoadingDescriptorsDefaults;
import ru.gxfin.core.base.db.dto.Derivative;
import ru.gxfin.core.base.db.dto.DerivativesPackage;

public class DerivativesLoadingDescriptor extends IncomeTopicLoadingDescriptor<Derivative, DerivativesPackage> {
    public DerivativesLoadingDescriptor(@NotNull String topic, IncomeTopicLoadingDescriptorsDefaults defaults) {
        super(topic, defaults);
    }
}
