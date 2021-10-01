package ru.gx.fin.base.db.dbcontroller;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import ru.gx.fin.base.db.events.DbControllerStartEvent;
import ru.gx.fin.base.db.events.DbControllerStopEvent;
import ru.gx.worker.SimpleWorker;

import static lombok.AccessLevel.*;

@Slf4j
public class DbControllerLifeController {
    @Getter
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private SimpleWorker simpleWorker;

    /**
     * Обработчик команды о запуске провайдера
     */
    @EventListener(DbControllerStartEvent.class)
    public void onEvent(DbControllerStartEvent __) {
        log.info("Starting onEvent(ProviderStartEvent event)");
        this.simpleWorker.start();
        log.info("Finished onEvent(ProviderStartEvent event)");
    }

    /**
     * Обработчик команды об остановке провайдера
     */
    @EventListener(DbControllerStopEvent.class)
    public void onEvent(DbControllerStopEvent __) {
        log.info("Starting onEvent(ProviderStopEvent event)");
        this.simpleWorker.stop();
        log.info("Finished onEvent(ProviderStopEvent event)");
    }
}
