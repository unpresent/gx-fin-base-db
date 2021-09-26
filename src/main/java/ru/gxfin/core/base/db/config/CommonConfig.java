package ru.gxfin.core.base.db.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
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
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.core.KafkaAdmin;
import ru.gxfin.common.kafka.TopicMessageMode;
import ru.gxfin.common.kafka.loader.LoadingMode;
import ru.gxfin.common.kafka.loader.StandardIncomeTopicsLoader;
import ru.gxfin.common.kafka.uploader.OutcomeTopicUploadingDescriptorsDefaults;
import ru.gxfin.core.base.db.converters.*;
import ru.gxfin.core.base.db.dbcontroller.DbController;
import ru.gxfin.core.base.db.dbcontroller.DbControllerLifeController;
import ru.gxfin.core.base.db.dbcontroller.DbControllerSettingsController;
import ru.gxfin.core.base.db.descriptors.*;
import ru.gxfin.core.base.db.events.LoadedDerivativesEvent;
import ru.gxfin.core.base.db.events.LoadedSecuritiesEvent;
import ru.gxfin.core.base.db.repository.*;
import ru.gxfin.core.base.db.utils.EntitiesUploader;
import ru.gxfin.core.base.db.memdata.*;

import java.util.HashMap;
import java.util.Properties;

@EnableJpaRepositories("ru.gxfin.core.base.db.repository")
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
    @SneakyThrows
    @Bean
    @Autowired
    public DbControllerSettingsController DbControllerSettingsController(ApplicationContext context) {
        return new DbControllerSettingsController(context);
    }

    @Bean
    public DbController DbController() {
        return new DbController(this.serviceName);
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
    @SneakyThrows
    @Bean
    @Autowired
    public InstrumentTypesMemoryRepository instrumentTypesMemoryRepository(ObjectMapper objectMapper) {
        return new InstrumentTypesMemoryRepository(objectMapper);
    }

    @SneakyThrows
    @Bean
    @Autowired
    public PlacesMemoryRepository placesMemoryRepository(ObjectMapper objectMapper) {
        return new PlacesMemoryRepository(objectMapper);
    }

    @SneakyThrows
    @Bean
    @Autowired
    public ProviderTypesMemoryRepository providerTypesMemoryRepository(ObjectMapper objectMapper) {
        return new ProviderTypesMemoryRepository(objectMapper);
    }

    @SneakyThrows
    @Bean
    @Autowired
    public ProvidersMemoryRepository providersMemoryRepository(ObjectMapper objectMapper) {
        return new ProvidersMemoryRepository(objectMapper);
    }

    @SneakyThrows
    @Bean
    @Autowired
    public CurrenciesMemoryRepository currenciesMemoryRepository(ObjectMapper objectMapper) {
        return new CurrenciesMemoryRepository(objectMapper);
    }

    @SneakyThrows
    @Bean
    @Autowired
    public SecuritiesMemoryRepository securitiesMemoryRepository(ObjectMapper objectMapper) {
        return new SecuritiesMemoryRepository(objectMapper);
    }

    @SneakyThrows
    @Bean
    @Autowired
    public DerivativesMemoryRepository derivativesMemoryRepository(ObjectMapper objectMapper) {
        return new DerivativesMemoryRepository(objectMapper);
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
    public StandardIncomeTopicsLoader incomeTopicsLoader(ApplicationContext context, ObjectMapper objectMapper, DbControllerSettingsController settings) {
        final var result = new StandardIncomeTopicsLoader(context, objectMapper);
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
            ObjectMapper objectMapper,
            DbControllerSettingsController settings,
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
        final var result = new EntitiesUploader(objectMapper);
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
