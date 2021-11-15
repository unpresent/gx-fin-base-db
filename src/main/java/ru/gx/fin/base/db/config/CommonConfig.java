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
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.core.KafkaAdmin;
import ru.gx.data.edlinking.EntitiesDtoLinksConfigurator;
import ru.gx.data.edlinking.EntitiesDtosLinksConfiguration;
import ru.gx.fin.base.db.converters.*;
import ru.gx.fin.base.db.dbcontroller.DbController;
import ru.gx.fin.base.db.dbcontroller.DbControllerSettingsContainer;
import ru.gx.fin.base.db.dto.*;
import ru.gx.fin.base.db.entities.*;
import ru.gx.fin.base.db.events.LoadedDerivativesEvent;
import ru.gx.fin.base.db.events.LoadedSecuritiesEvent;
import ru.gx.fin.base.db.memdata.*;
import ru.gx.fin.base.db.repository.*;
import ru.gx.kafka.SerializeMode;
import ru.gx.kafka.TopicMessageMode;
import ru.gx.kafka.load.IncomeTopicsConfiguration;
import ru.gx.kafka.load.IncomeTopicsConfigurator;
import ru.gx.kafka.load.LoadingMode;
import ru.gx.kafka.load.StandardIncomeTopicLoadingDescriptor;
import ru.gx.kafka.upload.OutcomeTopicsConfiguration;
import ru.gx.kafka.upload.OutcomeTopicsConfigurator;
import ru.gx.kafka.upload.StandardOutcomeTopicUploadingDescriptor;

import java.util.HashMap;
import java.util.Properties;

import static lombok.AccessLevel.PROTECTED;

@EnableJpaRepositories("ru.gx.fin.base.db.repository")
@EntityScan({"ru.gx.fin.base.db.entities"})
@EnableConfigurationProperties(ConfigurationPropertiesKafka.class)
public abstract class CommonConfig implements IncomeTopicsConfigurator, OutcomeTopicsConfigurator, EntitiesDtoLinksConfigurator {
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Common">
    @Value("${service.name}")
    private String serviceName;

    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private DbControllerSettingsContainer settings;

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
    private SecurityEntityFromDtoConverter securityEntityFromDtoConverter;

    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private DerivativeDtoFromEntityConverter derivativeDtoFromEntityConverter;

    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private DerivativeEntityFromDtoConverter derivativeEntityFromDtoConverter;

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
    public DbControllerSettingsContainer dbControllerSettingsContainer() {
        return new DbControllerSettingsContainer();
    }

    @Bean
    public DbController dbController() {
        return new DbController();
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

    @Bean
    public KafkaAdmin kafkaAdmin() {
        final var configs = new HashMap<String, Object>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        return new KafkaAdmin(configs);
    }

    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Kafka Consumers">
    private Properties consumerProperties() {
        final var properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, this.serviceName);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        return properties;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void configureIncomeTopics(@NotNull IncomeTopicsConfiguration incomeTopicsConfiguration) {
        incomeTopicsConfiguration.getDescriptorsDefaults()
                .setTopicMessageMode(TopicMessageMode.Package)
                .setLoadingMode(LoadingMode.Auto)
                .setPartitions(0)
                .setConsumerProperties(consumerProperties());

        incomeTopicsConfiguration
                .newDescriptor(this.settings.getIncomeTopicSecurities(), StandardIncomeTopicLoadingDescriptor.class)
                .setDataObjectClass(Security.class)
                .setDataPackageClass(SecuritiesPackage.class)
                .setOnLoadedEventClass(LoadedSecuritiesEvent.class)
                .setPriority(0)
                .init();

        incomeTopicsConfiguration
                .newDescriptor(this.settings.getIncomeTopicDerivatives(), StandardIncomeTopicLoadingDescriptor.class)
                .setDataObjectClass(Derivative.class)
                .setDataPackageClass(DerivativesPackage.class)
                .setOnLoadedEventClass(LoadedDerivativesEvent.class)
                .setPriority(1)
                .init();

    }

    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Uploading">
    private Properties producerProperties() {
        final var props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void configureOutcomeTopics(@NotNull OutcomeTopicsConfiguration configuration) {
        configuration.getDescriptorsDefaults()
                .setSerializeMode(SerializeMode.String)
                .setTopicMessageMode(TopicMessageMode.Object)
                .setProducerProperties(producerProperties());

        configuration
                .newDescriptor(settings.getOutcomeTopicPlaces(), StandardOutcomeTopicUploadingDescriptor.class)
                .setDataObjectClass(Place.class)
                .setDataPackageClass(PlacesPackage.class)
                .setMemoryRepository(this.placesMemoryRepository)
                .setPriority(0)
                .init();
        configuration
                .newDescriptor(settings.getOutcomeTopicProviderTypes(), StandardOutcomeTopicUploadingDescriptor.class)
                .setDataObjectClass(ProviderType.class)
                .setDataPackageClass(ProviderTypesPackage.class)
                .setMemoryRepository(this.providerTypesMemoryRepository)
                .setPriority(1)
                .init();
        configuration
                .newDescriptor(settings.getOutcomeTopicProviders(), StandardOutcomeTopicUploadingDescriptor.class)
                .setDataObjectClass(Provider.class)
                .setDataPackageClass(ProvidersPackage.class)
                .setMemoryRepository(this.providersMemoryRepository)
                .setPriority(2)
                .init();
        configuration
                .newDescriptor(settings.getOutcomeTopicInstrumentTypes(), StandardOutcomeTopicUploadingDescriptor.class)
                .setDataObjectClass(InstrumentType.class)
                .setDataPackageClass(InstrumentTypesPackage.class)
                .setMemoryRepository(this.instrumentTypesMemoryRepository)
                .setPriority(4)
                .init();
        configuration
                .newDescriptor(settings.getOutcomeTopicCurrencies(), StandardOutcomeTopicUploadingDescriptor.class)
                .setDataObjectClass(Currency.class)
                .setDataPackageClass(CurrenciesPackage.class)
                .setMemoryRepository(this.currenciesMemoryRepository)
                .setPriority(5)
                .init();
        configuration
                .newDescriptor(settings.getOutcomeTopicSecurities(), StandardOutcomeTopicUploadingDescriptor.class)
                .setDataObjectClass(Security.class)
                .setDataPackageClass(SecuritiesPackage.class)
                .setMemoryRepository(this.securitiesMemoryRepository)
                .setPriority(6)
                .init();
        configuration
                .newDescriptor(settings.getIncomeTopicDerivatives(), StandardOutcomeTopicUploadingDescriptor.class)
                .setDataObjectClass(Derivative.class)
                .setDataPackageClass(DerivativesPackage.class)
                .setMemoryRepository(this.derivativesMemoryRepository)
                .setPriority(7)
                .init();
    }

    @Override
    public void configureLinks(@NotNull EntitiesDtosLinksConfiguration configuration) {
        configuration
                .<PlaceEntity, PlaceEntitiesPackage, Short, Place, PlacesPackage>
                        newDescriptor(PlaceEntity.class, Place.class)
                .setDtoPackageClass(PlacesPackage.class)
                .setRepository(this.placesRepository)
                .setMemoryRepository(this.placesMemoryRepository)
                .setDtoFromEntityConverter(this.placeDtoFromEntityConverter);
        configuration
                .<ProviderTypeEntity, ProviderTypeEntitiesPackage, Short, ProviderType, ProviderTypesPackage>
                        newDescriptor(ProviderTypeEntity.class, ProviderType.class)
                .setDtoPackageClass(ProviderTypesPackage.class)
                .setRepository(this.providerTypesRepository)
                .setMemoryRepository(this.providerTypesMemoryRepository)
                .setDtoFromEntityConverter(this.providerTypeDtoFromEntityConverter);
        configuration
                .<ProviderEntity, ProviderEntitiesPackage, Short, Provider, ProvidersPackage>
                        newDescriptor(ProviderEntity.class, Provider.class)
                .setDtoPackageClass(ProvidersPackage.class)
                .setRepository(this.providersRepository)
                .setMemoryRepository(this.providersMemoryRepository)
                .setDtoFromEntityConverter(this.providerDtoFromEntityConverter);
        configuration
                .<InstrumentTypeEntity, InstrumentTypeEntitiesPackage, Short, InstrumentType, InstrumentTypesPackage>
                        newDescriptor(InstrumentTypeEntity.class, InstrumentType.class)
                .setDtoPackageClass(InstrumentTypesPackage.class)
                .setRepository(this.instrumentTypesRepository)
                .setMemoryRepository(this.instrumentTypesMemoryRepository)
                .setDtoFromEntityConverter(this.instrumentTypeDtoFromEntityConverter);
        configuration
                .<CurrencyEntity, CurrencyEntitiesPackage, Integer, Currency, CurrenciesPackage>
                        newDescriptor(CurrencyEntity.class, Currency.class)
                .setDtoPackageClass(CurrenciesPackage.class)
                .setRepository(this.currenciesRepository)
                .setMemoryRepository(this.currenciesMemoryRepository)
                .setDtoFromEntityConverter(this.currencyDtoFromEntityConverter);
        configuration
                .<SecurityEntity, SecurityEntitiesPackage, Integer, Security, SecuritiesPackage>
                        newDescriptor(SecurityEntity.class, Security.class)
                .setDtoPackageClass(SecuritiesPackage.class)
                .setRepository(this.securitiesRepository)
                .setMemoryRepository(this.securitiesMemoryRepository)
                .setDtoFromEntityConverter(this.securityDtoFromEntityConverter)
                .setEntityFromDtoConverter(this.securityEntityFromDtoConverter);
        configuration
                .<DerivativeEntity, DerivativeEntitiesPackage, Integer, Derivative, DerivativesPackage>
                        newDescriptor(DerivativeEntity.class, Derivative.class)
                .setDtoPackageClass(DerivativesPackage.class)
                .setRepository(this.derivativesRepository)
                .setMemoryRepository(this.derivativesMemoryRepository)
                .setDtoFromEntityConverter(this.derivativeDtoFromEntityConverter)
                .setEntityFromDtoConverter(this.derivativeEntityFromDtoConverter);
    }
    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
}
