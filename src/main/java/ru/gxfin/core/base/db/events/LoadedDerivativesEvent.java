package ru.gxfin.core.base.db.events;

import ru.gxfin.common.kafka.events.AbstractOnObjectsLoadedFromIncomeTopicEvent;
import ru.gxfin.core.base.db.dto.Derivative;
import ru.gxfin.core.base.db.dto.DerivativesPackage;

public class LoadedDerivativesEvent extends AbstractOnObjectsLoadedFromIncomeTopicEvent<Derivative, DerivativesPackage> {
    public LoadedDerivativesEvent(Object source) {
        super(source);
    }
}
