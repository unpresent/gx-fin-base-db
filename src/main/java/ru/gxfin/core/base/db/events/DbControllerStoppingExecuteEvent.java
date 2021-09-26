package ru.gxfin.core.base.db.events;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.gxfin.common.worker.AbstractStoppingExecuteEvent;

/**
 * Событие-сигнал о необходимости перезапустить DbController
 * @since 1.0
 */
@ToString
@EqualsAndHashCode(callSuper = false)
public class DbControllerStoppingExecuteEvent extends AbstractStoppingExecuteEvent {
    public DbControllerStoppingExecuteEvent(Object source) {
        super(source);
    }
}