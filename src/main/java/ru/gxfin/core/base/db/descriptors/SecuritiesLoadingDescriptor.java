package ru.gxfin.core.base.db.descriptors;

import org.jetbrains.annotations.NotNull;
import ru.gxfin.common.kafka.loader.IncomeTopicLoadingDescriptor;
import ru.gxfin.common.kafka.loader.IncomeTopicLoadingDescriptorsDefaults;
import ru.gxfin.core.base.db.dto.SecuritiesPackage;
import ru.gxfin.core.base.db.dto.Security;

public class SecuritiesLoadingDescriptor extends IncomeTopicLoadingDescriptor<Security, SecuritiesPackage> {
    public SecuritiesLoadingDescriptor(@NotNull String topic, IncomeTopicLoadingDescriptorsDefaults defaults) {
        super(topic, defaults);
    }
}
