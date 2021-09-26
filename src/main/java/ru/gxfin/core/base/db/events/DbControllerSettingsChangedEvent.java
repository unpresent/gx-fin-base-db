package ru.gxfin.core.base.db.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Событие об изменнении параметра системы
 * @since 1.0
 */
public class DbControllerSettingsChangedEvent extends ApplicationEvent {
    /**
     * Имя параметра, который изменился
     */
    @Getter
    private final String settingName;

    public DbControllerSettingsChangedEvent(Object source, String settingName) {
        super(source);
        this.settingName = settingName;
    }
}
