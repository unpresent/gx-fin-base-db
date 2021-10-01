package ru.gx.fin.base.db.events;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEvent;

/**
 * Событие-сигнал о необходимости перезапустить DbController
 * @since 1.0
 */
@ToString
@EqualsAndHashCode(callSuper = false)
public class DbControllerStartEvent extends ApplicationEvent {
    public DbControllerStartEvent(@NotNull final Object source) {
        super(source);
    }
}