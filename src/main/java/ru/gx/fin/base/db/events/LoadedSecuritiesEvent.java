package ru.gx.fin.base.db.events;

import org.jetbrains.annotations.NotNull;
import ru.gx.fin.base.db.dto.SecuritiesPackage;
import ru.gx.kafka.events.AbstractOnObjectsLoadedFromIncomeTopicEvent;
import ru.gx.fin.base.db.dto.Security;

public class LoadedSecuritiesEvent extends AbstractOnObjectsLoadedFromIncomeTopicEvent<Security, SecuritiesPackage> {
    public LoadedSecuritiesEvent(@NotNull final Object source) {
        super(source);
    }
}
