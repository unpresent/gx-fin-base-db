package ru.gx.fin.base.db.events;

import org.jetbrains.annotations.NotNull;
import ru.gx.fin.base.db.dto.Derivative;
import ru.gx.fin.base.db.dto.DerivativesPackage;
import ru.gx.kafka.events.AbstractOnObjectsLoadedFromIncomeTopicEvent;

public class LoadedDerivativesEvent extends AbstractOnObjectsLoadedFromIncomeTopicEvent<Derivative, DerivativesPackage> {
    public LoadedDerivativesEvent(@NotNull final Object source) {
        super(source);
    }
}
