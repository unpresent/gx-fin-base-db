package ru.gxfin.core.base.db.events;

import ru.gxfin.common.worker.AbstractIterationExecuteEvent;

public class DbControllerIterationExecuteEvent extends AbstractIterationExecuteEvent {
    public DbControllerIterationExecuteEvent(Object source) {
        super(source);
    }
}
