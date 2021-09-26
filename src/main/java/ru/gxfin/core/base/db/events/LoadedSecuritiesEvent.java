package ru.gxfin.core.base.db.events;

import ru.gxfin.common.kafka.events.AbstractOnObjectsLoadedFromIncomeTopicEvent;
import ru.gxfin.core.base.db.dto.SecuritiesPackage;
import ru.gxfin.core.base.db.dto.Security;

public class LoadedSecuritiesEvent extends AbstractOnObjectsLoadedFromIncomeTopicEvent<Security, SecuritiesPackage> {
    public LoadedSecuritiesEvent(Object source) {
        super(source);
    }
}
