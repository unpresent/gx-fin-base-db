package ru.gx.fin.base.db.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Setter;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.core.KafkaAdmin;
import ru.gx.fin.base.db.converters.*;
import ru.gx.fin.base.db.dbcontroller.DbController;
import ru.gx.fin.base.db.dbcontroller.DbControllerLifeController;
import ru.gx.fin.base.db.dbcontroller.DbControllerSettingsContainer;
import ru.gx.fin.base.db.descriptors.*;
import ru.gx.fin.base.db.events.LoadedDerivativesEvent;
import ru.gx.fin.base.db.events.LoadedSecuritiesEvent;
import ru.gx.fin.base.db.memdata.*;
import ru.gx.fin.base.db.repository.*;
import ru.gx.kafka.TopicMessageMode;
import ru.gx.kafka.load.IncomeTopicsConfiguration;
import ru.gx.kafka.load.IncomeTopicsConfigurator;
import ru.gx.kafka.load.LoadingMode;
import ru.gx.kafka.upload.OutcomeTopicUploadingDescriptorsDefaults;
import ru.gx.std.upload.EntitiesUploader;
import ru.gx.std.upload.EntitiesUploaderConfigurator;

import java.util.HashMap;
import java.util.Properties;

import static lombok.AccessLevel.PROTECTED;

@EnableJpaRepositories("ru.gx.fin.base.db.repository")
@EntityScan({"ru.gx.fin.base.db.entities"})
public abstract class CommonConfig implements IncomeTopicsConfigurator, EntitiesUploaderConfigurator {
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Common">
    @Value("${service.name}")
    private String serviceName;

    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private DbControllerSettingsContainer settings;

    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private KafkaProducer<Long, String> producer;

    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private PlaceDtoFromEntityConverter placeDtoFromEntityConverter;

    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private ProviderTypeDtoFromEntityConverter providerTypeDtoFromEntityConverter;

    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private ProviderDtoFromEntityConverter providerDtoFromEntityConverter;

    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private InstrumentTypeDtoFromEntityConverter instrumentTypeDtoFromEntityConverter;

    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private CurrencyDtoFromEntityConverter currencyDtoFromEntityConverter;

    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private SecurityDtoFromEntityConverter securityDtoFromEntityConverter;

    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private DerivativeDtoFromEntityConverter derivativeDtoFromEntityConverter;

    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private PlacesRepository placesRepository;

    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private ProviderTypesRepository providerTypesRepository;

    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private ProvidersRepository providersRepository;

    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private InstrumentTypesRepository instrumentTypesRepository;

    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private CurrenciesRepository currenciesRepository;

    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private SecuritiesRepository securitiesRepository;

    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private DerivativesRepository derivativesRepository;

    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private PlacesMemoryRepository placesMemoryRepository;

    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private ProviderTypesMemoryRepository providerTypesMemoryRepository;

    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private ProvidersMemoryRepository providersMemoryRepository;

    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private InstrumentTypesMemoryRepository instrumentTypesMemoryRepository;

    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private CurrenciesMemoryRepository currenciesMemoryRepository;

    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private SecuritiesMemoryRepository securitiesMemoryRepository;

    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private DerivativesMemoryRepository derivativesMemoryRepository;

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

    @Override
    public void configureIncomeTopics(@NotNull IncomeTopicsConfiguration incomeTopicsConfiguration) {
        final var defaults = incomeTopicsConfiguration.getDescriptorsDefaults();
        incomeTopicsConfiguration.getDescriptorsDefaults()
                .setTopicMessageMode(TopicMessageMode.OBJECT)
                .setLoadingMode(LoadingMode.Auto)
                .setConsumerProperties(consumerProperties())
                .setPartitions(0);

        incomeTopicsConfiguration
                .register(
                        new SecuritiesLoadingDescriptor(settings.getIncomeTopicSecurities(), defaults)
                                .setPriority(0)
                                .setOnLoadedEventClass(LoadedSecuritiesEvent.class)
                )
                .register(
                        new DerivativesLoadingDescriptor(settings.getIncomeTopicDerivatives(), defaults)
                                .setPriority(1)
                                .setOnLoadedEventClass(LoadedDerivativesEvent.class)
                );
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

    @Override
    public void configureEntitiesUploader(@NotNull EntitiesUploader entitiesUploader) {
        final var defaults = new OutcomeTopicUploadingDescriptorsDefaults()
                .setTopicMessageMode(TopicMessageMode.OBJECT)
                .setProducer(producer);

        entitiesUploader
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

    }
    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
}
