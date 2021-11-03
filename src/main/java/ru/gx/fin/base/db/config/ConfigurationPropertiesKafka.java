package ru.gx.fin.base.db.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(prefix = "kafka")
@Getter
@Setter
public class ConfigurationPropertiesKafka {
    public static final String INCOME_TOPIC_SECURITIES = "kafka.income-topics.securities";
    public static final String INCOME_TOPIC_DERIVATIVES = "kafka.income-topics.derivatives";

    public static final String OUTCOME_TOPIC_PLACES = "kafka.outcome-topics.places";
    public static final String OUTCOME_TOPIC_PROVIDER_TYPES = "kafka.outcome-topics.provider-types";
    public static final String OUTCOME_TOPIC_PROVIDERS = "kafka.outcome-topics.providers";
    public static final String OUTCOME_TOPIC_INSTRUMENT_TYPES = "kafka.outcome-topics.instrument-types";
    public static final String OUTCOME_TOPIC_CURRENCIES = "kafka.outcome-topics.currencies";
    public static final String OUTCOME_TOPIC_SECURITIES = "kafka.outcome-topics.securities";
    public static final String OUTCOME_TOPIC_DERIVATIVES = "kafka.outcome-topics.derivatives";

    @NestedConfigurationProperty
    private IncomeTopics incomeTopics = new IncomeTopics();

    @NestedConfigurationProperty
    private OutcomeTopics outcomeTopics = new OutcomeTopics();

    @Getter
    @Setter
    private static class IncomeTopics {
        private String securities = "baseSecuritiesRequests";
        private String derivatives = "baseDerivativesRequests";
    }

    @Getter
    @Setter
    private static class OutcomeTopics {
        private String places = "basePlaces";
        private String providerTypes = "baseProviderTypes";
        private String providers = "baseProviders";
        private String instrumentTypes = "baseInstrumentTypes";
        private String currencies = "baseCurrencies";
        private String securities = "baseSecurities";
        private String derivatives = "baseDerivatives";
    }
}
