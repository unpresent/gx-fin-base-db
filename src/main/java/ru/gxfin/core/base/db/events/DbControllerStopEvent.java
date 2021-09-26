package ru.gxfin.core.base.db.events;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

/**
 * Событие-сигнал о необходимости остановить DbController
 * @since 1.0
 */
@ToString
@EqualsAndHashCode(callSuper = true)
public class DbControllerStopEvent extends ApplicationEvent {
    public DbControllerStopEvent(Object source) {
        super(source);
    }
}