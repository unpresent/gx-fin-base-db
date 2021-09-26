package ru.gxfin.core.base.db.dbcontroller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import ru.gxfin.core.base.db.events.DbControllerStartEvent;
import ru.gxfin.core.base.db.events.DbControllerStopEvent;

@Slf4j
public class DbControllerLifeController {
    @Autowired
    private DbController dbController;

    /**
     * Обработчик команды о запуске провайдера
     * @param event команда о запуске провайдера
     */
    @EventListener(DbControllerStartEvent.class)
    public void onEvent(DbControllerStartEvent event) {
        log.info("Starting onEvent(ProviderStartEvent event)");
        this.dbController.start();
        log.info("Finished onEvent(ProviderStartEvent event)");
    }

    /**
     * Обработчик команды об остановке провайдера
     * @param event команда об остановке провайдера
     */
    @EventListener(DbControllerStopEvent.class)
    public void onEvent(DbControllerStopEvent event) {
        log.info("Starting onEvent(ProviderStopEvent event)");
        this.dbController.stop();
        log.info("Finished onEvent(ProviderStopEvent event)");
    }
}
