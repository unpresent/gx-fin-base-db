package ru.gxfin.core.base.db.dbcontroller;

import org.springframework.context.ApplicationContext;
import ru.gxfin.common.settings.AbstractSettingsController;
import ru.gxfin.common.settings.UnknownApplicationSettingException;

import java.time.Duration;

public class DbControllerSettingsController extends AbstractSettingsController {
    String WAIT_ON_STOP_MS = "worker.wait_on_stop_ms";
    String WAIT_ON_RESTART_MS = "worker.wait_on_restart_ms";
    String MIN_TIME_PER_ITERATION_MS = "worker.min_time_per_iteration_ms";
    String TIMEOUT_LIFE_MS = "worker.timeout_life_ms";

    String DURATION_ON_POLL_MS = "kafka.duration_on_poll_ms";

    String INCOME_TOPIC_SECURITIES = "kafka.income_topic.securities";
    String INCOME_TOPIC_DERIVATIVES = "kafka.income_topic.derivatives";

    String OUTCOME_TOPIC_PLACES = "kafka.outcome_topic.places";
    String OUTCOME_TOPIC_PROVIDER_TYPES = "kafka.outcome_topic.provider_types";
    String OUTCOME_TOPIC_PROVIDERS = "kafka.outcome_topic.providers";
    String OUTCOME_TOPIC_INSTRUMENT_TYPES = "kafka.outcome_topic.instrument_types";
    String OUTCOME_TOPIC_CURRENCIES = "kafka.outcome_topic.currencies";
    String OUTCOME_TOPIC_SECURITIES = "kafka.outcome_topic.securities";
    String OUTCOME_TOPIC_DERIVATIVES = "kafka.outcome_topic.derivatives";

    public DbControllerSettingsController(ApplicationContext context) throws UnknownApplicationSettingException {
        super(context);

        // TODO: Переписать на чтение настроек
        loadIntegerSetting(WAIT_ON_STOP_MS);
        loadIntegerSetting(WAIT_ON_RESTART_MS);
        loadIntegerSetting(MIN_TIME_PER_ITERATION_MS);
        loadIntegerSetting(TIMEOUT_LIFE_MS);
        loadIntegerSetting(DURATION_ON_POLL_MS);

        loadStringSetting(INCOME_TOPIC_SECURITIES);
        loadStringSetting(INCOME_TOPIC_DERIVATIVES);

        loadStringSetting(OUTCOME_TOPIC_PLACES);
        loadStringSetting(OUTCOME_TOPIC_PROVIDER_TYPES);
        loadStringSetting(OUTCOME_TOPIC_PROVIDERS);
        loadStringSetting(OUTCOME_TOPIC_INSTRUMENT_TYPES);
        loadStringSetting(OUTCOME_TOPIC_CURRENCIES);
        loadStringSetting(OUTCOME_TOPIC_SECURITIES);
        loadStringSetting(OUTCOME_TOPIC_DERIVATIVES);
    }

    public int getWaitOnStopMs() {
        return (Integer) getSetting(WAIT_ON_STOP_MS);
    }

    public int getWaitOnRestartMs() {
        return (Integer) getSetting(WAIT_ON_RESTART_MS);
    }

    public int getMinTimePerIterationMs() {
        return (Integer) getSetting(MIN_TIME_PER_ITERATION_MS);
    }

    public int getTimeoutLifeMs() {
        return (Integer) getSetting(TIMEOUT_LIFE_MS);
    }

    public Duration getDurationOnPollMs() {
        return Duration.ofMillis((Integer) this.getSetting(DURATION_ON_POLL_MS));
    }

    public String getIncomeTopicSecurities() {
        return (String) getSetting(INCOME_TOPIC_SECURITIES);
    }

    public String getIncomeTopicDerivatives() {
        return (String) getSetting(INCOME_TOPIC_DERIVATIVES);
    }

    public String getOutcomeTopicPlaces() {
        return (String) getSetting(OUTCOME_TOPIC_PLACES);
    }

    public String getOutcomeTopicProviderTypes() {
        return (String) getSetting(OUTCOME_TOPIC_PROVIDER_TYPES);
    }

    public String getOutcomeTopicProviders() {
        return (String) getSetting(OUTCOME_TOPIC_PROVIDERS);
    }

    public String getOutcomeTopicInstrumentTypes() {
        return (String) getSetting(OUTCOME_TOPIC_INSTRUMENT_TYPES);
    }

    public String getOutcomeTopicCurrencies() {
        return (String) getSetting(OUTCOME_TOPIC_CURRENCIES);
    }

    public String getOutcomeTopicSecurities() {
        return (String) getSetting(OUTCOME_TOPIC_SECURITIES);
    }

    public String getOutcomeTopicDerivatives() {
        return (String) getSetting(OUTCOME_TOPIC_DERIVATIVES);
    }
}
