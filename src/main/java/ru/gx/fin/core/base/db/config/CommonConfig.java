package ru.gx.fin.core.base.db.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.core.KafkaAdmin;
import ru.gx.fin.core.base.db.converters.*;
import ru.gx.fin.core.base.db.dbcontroller.DbController;
import ru.gx.fin.core.base.db.dbcontroller.DbControllerLifeController;
import ru.gx.fin.core.base.db.dbcontroller.DbControllerSettingsContainer;
import ru.gx.fin.core.base.db.descriptors.*;
import ru.gx.fin.core.base.db.events.LoadedDerivativesEvent;
import ru.gx.fin.core.base.db.events.LoadedSecuritiesEvent;
import ru.gx.fin.core.base.db.memdata.*;
import ru.gx.fin.core.base.db.repository.*;
import ru.gx.kafka.TopicMessageMode;
import ru.gx.kafka.load.LoadingMode;
import ru.gx.kafka.load.SimpleIncomeTopicsLoader;
import ru.gx.kafka.upload.EntitiesUploader;
import ru.gx.kafka.upload.OutcomeTopicUploadingDescriptorsDefaults;

import java.util.HashMap;
import java.util.Properties;

@EnableJpaRepositories("ru.gx.fin.core.base.db.repository")
@EntityScan({"ru.gx.fin.core.base.db.entities"})
public abstract class CommonConfig {
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Common">
    @Value("${service.name}")
    private String serviceName;

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule());
    }

    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Worker & Settings">
    @Bean
    public DbControllerSettingsContainer DbControllerSettingsController() {
        return new DbControllerSettingsContainer();
    }

    @Bean
    public DbController DbController() {
        return new DbController();
    }

    @Bean
    public DbControllerLifeController DbControllerLifeController() {
        return new DbControllerLifeController();
    }

    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Converters">
    @Bean
    public CurrencyDtoFromEntityConverter currencyDtoFromEntityConverter() {
        return new CurrencyDtoFromEntityConverter();
    }

    @Bean
    public CurrencyEntityFromDtoConverter currencyEntityFromDtoConverter() {
        return new CurrencyEntityFromDtoConverter();
    }

    @Bean
    public DerivativeDtoFromEntityConverter derivativeDtoFromEntityConverter() {
        return new DerivativeDtoFromEntityConverter();
    }

    @Bean
    public DerivativeEntityFromDtoConverter derivativeEntityFromDtoConverter() {
        return new DerivativeEntityFromDtoConverter();
    }

    @Bean
    public InstrumentTypeDtoFromEntityConverter instrumentTypeDtoFromEntityConverter() {
        return new InstrumentTypeDtoFromEntityConverter();
    }

    @Bean
    public InstrumentTypeEntityFromDtoConverter instrumentTypeEntityFromDtoConverter() {
        return new InstrumentTypeEntityFromDtoConverter();
    }

    @Bean
    public PlaceDtoFromEntityConverter placeDtoFromEntityConverter() {
        return new PlaceDtoFromEntityConverter();
    }

    @Bean
    public PlaceEntityFromDtoConverter placeEntityFromDtoConverter() {
        return new PlaceEntityFromDtoConverter();
    }

    @Bean
    public ProviderDtoFromEntityConverter providerDtoFromEntityConverter() {
        return new ProviderDtoFromEntityConverter();
    }

    @Bean
    public ProviderEntityFromDtoConverter providerEntityFromDtoConverter() {
        return new ProviderEntityFromDtoConverter();
    }

    @Bean
    public ProviderTypeDtoFromEntityConverter providerTypeDtoFromEntityConverter() {
        return new ProviderTypeDtoFromEntityConverter();
    }

    @Bean
    public ProviderTypeEntityFromDtoConverter providerTypeEntityFromDtoConverter() {
        return new ProviderTypeEntityFromDtoConverter();
    }

    @Bean
    public SecurityDtoFromEntityConverter securityDtoFromEntityConverter() {
        return new SecurityDtoFromEntityConverter();
    }

    @Bean
    public SecurityEntityFromDtoConverter securityEntityFromDtoConverter() {
        return new SecurityEntityFromDtoConverter();
    }

    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="MemoryRepositories">
    @Bean
    public InstrumentTypesMemoryRepository instrumentTypesMemoryRepository() {
        return new InstrumentTypesMemoryRepository();
    }

    @Bean
    public PlacesMemoryRepository placesMemoryRepository() {
        return new PlacesMemoryRepository();
    }

    @Bean
    public ProviderTypesMemoryRepository providerTypesMemoryRepository() {
        return new ProviderTypesMemoryRepository();
    }

    @Bean
    public ProvidersMemoryRepository providersMemoryRepository() {
        return new ProvidersMemoryRepository();
    }

    @Bean
    public CurrenciesMemoryRepository currenciesMemoryRepository() {
        return new CurrenciesMemoryRepository();
    }

    @Bean
    public SecuritiesMemoryRepository securitiesMemoryRepository() {
        return new SecuritiesMemoryRepository();
    }

    @Bean
    public DerivativesMemoryRepository derivativesMemoryRepository() {
        return new DerivativesMemoryRepository();
    }

    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Events">
    @Bean
    @Autowired
    public LoadedDerivativesEvent loadedDerivativesEvent(DbController DbController) {
        return new LoadedDerivativesEvent(DbController);
    }

    @Bean
    @Autowired
    public LoadedSecuritiesEvent loadedSecuritiesEvent(DbController DbController) {
        return new LoadedSecuritiesEvent(DbController);
    }

    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Kafka Common">
    @Value(value = "${kafka.server}")
    private String kafkaServer;

    @Value("${kafka.group_id}")
    private String kafkaGroupId;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        final var configs = new HashMap<String, Object>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        return new KafkaAdmin(configs);
    }

    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Kafka Consumers">
    public Properties consumerProperties() {
        final var props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return props;
    }

    @Bean
    @Autowired
    public SimpleIncomeTopicsLoader incomeTopicsLoader(DbControllerSettingsContainer settings) {
        final var result = new SimpleIncomeTopicsLoader(this.serviceName);
        result.getDescriptorsDefaults()
                .setTopicMessageMode(TopicMessageMode.OBJECT)
                .setLoadingMode(LoadingMode.Auto)
                .setConsumerProperties(consumerProperties())
                .setPartitions(0);

        result
                .register(
                        new SecuritiesLoadingDescriptor(settings.getIncomeTopicSecurities(), result.getDescriptorsDefaults())
                                .setPriority(0)
                                .setOnLoadedEventClass(LoadedSecuritiesEvent.class)
                )
                .register(
                        new DerivativesLoadingDescriptor(settings.getIncomeTopicDerivatives(), result.getDescriptorsDefaults())
                                .setPriority(1)
                                .setOnLoadedEventClass(LoadedDerivativesEvent.class)
                );

        return result;
    }

    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Uploading">
    @Bean
    public KafkaProducer<Long, String> producer() {
        final var props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new KafkaProducer<>(props);
    }

    @Bean
    @Autowired
    public EntitiesUploader entitiesUploader(
            DbControllerSettingsContainer settings,
            KafkaProducer<Long, String> producer,
            PlaceDtoFromEntityConverter placeDtoFromEntityConverter,
            ProviderTypeDtoFromEntityConverter providerTypeDtoFromEntityConverter,
            ProviderDtoFromEntityConverter providerDtoFromEntityConverter,
            InstrumentTypeDtoFromEntityConverter instrumentTypeDtoFromEntityConverter,
            CurrencyDtoFromEntityConverter currencyDtoFromEntityConverter,
            SecurityDtoFromEntityConverter securityDtoFromEntityConverter,
            DerivativeDtoFromEntityConverter derivativeDtoFromEntityConverter,
            PlacesRepository placesRepository,
            ProviderTypesRepository providerTypesRepository,
            ProvidersRepository providersRepository,
            InstrumentTypesRepository instrumentTypesRepository,
            CurrenciesRepository currenciesRepository,
            SecuritiesRepository securitiesRepository,
            DerivativesRepository derivativesRepository,
            PlacesMemoryRepository placesMemoryRepository,
            ProviderTypesMemoryRepository providerTypesMemoryRepository,
            ProvidersMemoryRepository providersMemoryRepository,
            InstrumentTypesMemoryRepository instrumentTypesMemoryRepository,
            CurrenciesMemoryRepository currenciesMemoryRepository,
            SecuritiesMemoryRepository securitiesMemoryRepository,
            DerivativesMemoryRepository derivativesMemoryRepository
    ) {
        final var result = new EntitiesUploader();
        final var defaults = new OutcomeTopicUploadingDescriptorsDefaults()
                .setTopicMessageMode(TopicMessageMode.OBJECT)
                .setProducer(producer);

        result
                .register(
                        new PlacesUploadingDescriptor(settings.getOutcomeTopicPlaces(), defaults)
                                .setConverter(placeDtoFromEntityConverter)
                                .setRepository(placesRepository)
                                .setMemoryRepository(placesMemoryRepository)
                )
                .register(
                        new ProviderTypesUploadingDescriptor(settings.getOutcomeTopicProviderTypes(), defaults)
                                .setConverter(providerTypeDtoFromEntityConverter)
                                .setRepository(providerTypesRepository)
                                .setMemoryRepository(providerTypesMemoryRepository)
                )
                .register(
                        new ProvidersUploadingDescriptor(settings.getOutcomeTopicProviders(), defaults)
                                .setConverter(providerDtoFromEntityConverter)
                                .setRepository(providersRepository)
                                .setMemoryRepository(providersMemoryRepository)
                )
                .register(
                        new InstrumentTypesUploadingDescriptor(settings.getOutcomeTopicInstrumentTypes(), defaults)
                                .setConverter(instrumentTypeDtoFromEntityConverter)
                                .setRepository(instrumentTypesRepository)
                                .setMemoryRepository(instrumentTypesMemoryRepository)
                )
                .register(
                        new CurrenciesUploadingDescriptor(settings.getOutcomeTopicCurrencies(), defaults)
                                .setConverter(currencyDtoFromEntityConverter)
                                .setRepository(currenciesRepository)
                                .setMemoryRepository(currenciesMemoryRepository)
                )
                .register(
                        new SecuritiesUploadingDescriptor(settings.getOutcomeTopicSecurities(), defaults)
                                .setConverter(securityDtoFromEntityConverter)
                                .setRepository(securitiesRepository)
                                .setMemoryRepository(securitiesMemoryRepository)
                )
                .register(
                        new DerivativesUploadingDescriptor(settings.getOutcomeTopicDerivatives(), defaults)
                                .setConverter(derivativeDtoFromEntityConverter)
                                .setRepository(derivativesRepository)
                                .setMemoryRepository(derivativesMemoryRepository)
                );

        return result;
    }
    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
}
