package ru.gx.fin.base.db.dbcontroller;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gx.settings.SimpleSettingsController;
import ru.gx.settings.UnknownApplicationSettingException;

import javax.annotation.PostConstruct;
import java.time.Duration;

import static lombok.AccessLevel.PROTECTED;

public class DbControllerSettingsContainer {
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

    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    @NotNull
    private SimpleSettingsController simpleSettingsController;

    @PostConstruct
    public void init() throws UnknownApplicationSettingException {
        this.simpleSettingsController.loadIntegerSetting(DURATION_ON_POLL_MS);

        this.simpleSettingsController.loadStringSetting(INCOME_TOPIC_SECURITIES);
        this.simpleSettingsController.loadStringSetting(INCOME_TOPIC_DERIVATIVES);

        this.simpleSettingsController.loadStringSetting(OUTCOME_TOPIC_PLACES);
        this.simpleSettingsController.loadStringSetting(OUTCOME_TOPIC_PROVIDER_TYPES);
        this.simpleSettingsController.loadStringSetting(OUTCOME_TOPIC_PROVIDERS);
        this.simpleSettingsController.loadStringSetting(OUTCOME_TOPIC_INSTRUMENT_TYPES);
        this.simpleSettingsController.loadStringSetting(OUTCOME_TOPIC_CURRENCIES);
        this.simpleSettingsController.loadStringSetting(OUTCOME_TOPIC_SECURITIES);
        this.simpleSettingsController.loadStringSetting(OUTCOME_TOPIC_DERIVATIVES);
    }

    public Duration getDurationOnPollMs() {
        return Duration.ofMillis(this.simpleSettingsController.getIntegerSetting(DURATION_ON_POLL_MS));
    }

    public String getIncomeTopicSecurities() {
        return this.simpleSettingsController.getStringSetting(INCOME_TOPIC_SECURITIES);
    }

    public String getIncomeTopicDerivatives() {
        return this.simpleSettingsController.getStringSetting(INCOME_TOPIC_DERIVATIVES);
    }

    public String getOutcomeTopicPlaces() {
        return this.simpleSettingsController.getStringSetting(OUTCOME_TOPIC_PLACES);
    }

    public String getOutcomeTopicProviderTypes() {
        return this.simpleSettingsController.getStringSetting(OUTCOME_TOPIC_PROVIDER_TYPES);
    }

    public String getOutcomeTopicProviders() {
        return this.simpleSettingsController.getStringSetting(OUTCOME_TOPIC_PROVIDERS);
    }

    public String getOutcomeTopicInstrumentTypes() {
        return this.simpleSettingsController.getStringSetting(OUTCOME_TOPIC_INSTRUMENT_TYPES);
    }

    public String getOutcomeTopicCurrencies() {
        return this.simpleSettingsController.getStringSetting(OUTCOME_TOPIC_CURRENCIES);
    }

    public String getOutcomeTopicSecurities() {
        return this.simpleSettingsController.getStringSetting(OUTCOME_TOPIC_SECURITIES);
    }

    public String getOutcomeTopicDerivatives() {
        return this.simpleSettingsController.getStringSetting(OUTCOME_TOPIC_DERIVATIVES);
    }
}
