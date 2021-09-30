package ru.gx.fin.core.base.db.events;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEvent;

/**
 * Событие-сигнал о необходимости остановить DbController
 * @since 1.0
 */
@ToString
@EqualsAndHashCode(callSuper = true)
public class DbControllerStopEvent extends ApplicationEvent {
    public DbControllerStopEvent(@NotNull final Object source) {
        super(source);
    }
}