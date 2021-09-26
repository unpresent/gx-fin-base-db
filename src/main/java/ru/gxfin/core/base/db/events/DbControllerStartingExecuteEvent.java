package ru.gxfin.core.base.db.events;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.gxfin.common.worker.AbstractStartingExecuteEvent;

/**
 * Событие-сигнал о необходимости перезапустить DbController
 * @since 1.0
 */
@ToString
@EqualsAndHashCode(callSuper = false)
public class DbControllerStartingExecuteEvent extends AbstractStartingExecuteEvent {
    public DbControllerStartingExecuteEvent(Object source) {
        super(source);
    }
}