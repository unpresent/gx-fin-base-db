package ru.gx.fin.core.base.db.events;

import org.jetbrains.annotations.NotNull;
import ru.gx.fin.core.base.db.dto.Derivative;
import ru.gx.fin.core.base.db.dto.DerivativesPackage;
import ru.gx.kafka.events.AbstractOnObjectsLoadedFromIncomeTopicEvent;

public class LoadedDerivativesEvent extends AbstractOnObjectsLoadedFromIncomeTopicEvent<Derivative, DerivativesPackage> {
    public LoadedDerivativesEvent(@NotNull final Object source) {
        super(source);
    }
}
