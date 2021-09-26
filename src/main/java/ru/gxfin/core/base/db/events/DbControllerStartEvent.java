package ru.gxfin.core.base.db.events;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

/**
 * Событие-сигнал о необходимости перезапустить DbController
 * @since 1.0
 */
@ToString
@EqualsAndHashCode(callSuper = false)
public class DbControllerStartEvent extends ApplicationEvent {
    public DbControllerStartEvent(Object source) {
        super(source);
    }
}