package ru.gx.fin.base.db.dbcontroller;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gx.fin.base.db.config.ConfigurationPropertiesKafka;
import ru.gx.settings.SimpleSettingsController;
import ru.gx.settings.UnknownApplicationSettingException;

import javax.annotation.PostConstruct;
import java.time.Duration;

import static lombok.AccessLevel.PROTECTED;

public class DbControllerSettingsContainer {
    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    @NotNull
    private SimpleSettingsController simpleSettingsController;

    @PostConstruct
    public void init() throws UnknownApplicationSettingException {
        this.simpleSettingsController.loadStringSetting(ConfigurationPropertiesKafka.INCOME_TOPIC_SECURITIES);
        this.simpleSettingsController.loadStringSetting(ConfigurationPropertiesKafka.INCOME_TOPIC_DERIVATIVES);

        this.simpleSettingsController.loadStringSetting(ConfigurationPropertiesKafka.OUTCOME_TOPIC_PLACES);
        this.simpleSettingsController.loadStringSetting(ConfigurationPropertiesKafka.OUTCOME_TOPIC_PROVIDER_TYPES);
        this.simpleSettingsController.loadStringSetting(ConfigurationPropertiesKafka.OUTCOME_TOPIC_PROVIDERS);
        this.simpleSettingsController.loadStringSetting(ConfigurationPropertiesKafka.OUTCOME_TOPIC_INSTRUMENT_TYPES);
        this.simpleSettingsController.loadStringSetting(ConfigurationPropertiesKafka.OUTCOME_TOPIC_CURRENCIES);
        this.simpleSettingsController.loadStringSetting(ConfigurationPropertiesKafka.OUTCOME_TOPIC_SECURITIES);
        this.simpleSettingsController.loadStringSetting(ConfigurationPropertiesKafka.OUTCOME_TOPIC_DERIVATIVES);
    }

    public String getIncomeTopicSecurities() {
        return this.simpleSettingsController.getStringSetting(ConfigurationPropertiesKafka.INCOME_TOPIC_SECURITIES);
    }

    public String getIncomeTopicDerivatives() {
        return this.simpleSettingsController.getStringSetting(ConfigurationPropertiesKafka.INCOME_TOPIC_DERIVATIVES);
    }

    public String getOutcomeTopicPlaces() {
        return this.simpleSettingsController.getStringSetting(ConfigurationPropertiesKafka.OUTCOME_TOPIC_PLACES);
    }

    public String getOutcomeTopicProviderTypes() {
        return this.simpleSettingsController.getStringSetting(ConfigurationPropertiesKafka.OUTCOME_TOPIC_PROVIDER_TYPES);
    }

    public String getOutcomeTopicProviders() {
        return this.simpleSettingsController.getStringSetting(ConfigurationPropertiesKafka.OUTCOME_TOPIC_PROVIDERS);
    }

    public String getOutcomeTopicInstrumentTypes() {
        return this.simpleSettingsController.getStringSetting(ConfigurationPropertiesKafka.OUTCOME_TOPIC_INSTRUMENT_TYPES);
    }

    public String getOutcomeTopicCurrencies() {
        return this.simpleSettingsController.getStringSetting(ConfigurationPropertiesKafka.OUTCOME_TOPIC_CURRENCIES);
    }

    public String getOutcomeTopicSecurities() {
        return this.simpleSettingsController.getStringSetting(ConfigurationPropertiesKafka.OUTCOME_TOPIC_SECURITIES);
    }

    public String getOutcomeTopicDerivatives() {
        return this.simpleSettingsController.getStringSetting(ConfigurationPropertiesKafka.OUTCOME_TOPIC_DERIVATIVES);
    }
}
