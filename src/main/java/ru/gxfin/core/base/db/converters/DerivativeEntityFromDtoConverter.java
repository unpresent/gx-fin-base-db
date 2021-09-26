package ru.gxfin.core.base.db.converters;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gxfin.common.data.AbstractEntityFromDtoConverter;
import ru.gxfin.core.base.db.entities.*;
import ru.gxfin.core.base.db.repository.*;
import ru.gxfin.core.base.db.dto.Derivative;
import ru.gxfin.core.base.db.dto.ProviderType;

import java.util.Objects;

public class DerivativeEntityFromDtoConverter extends AbstractEntityFromDtoConverter<DerivativeEntity, DerivativeEntitiesPackage, Derivative> {
    @Autowired
    private CurrenciesRepository currenciesRepository;

    @Autowired
    private SecuritiesRepository securitiesRepository;

    @Autowired
    private DerivativesRepository derivativesRepository;

    @Autowired
    private InstrumentTypesRepository instrumentTypesRepository;

    @Override
    public void fillEntityFromDto(@NotNull DerivativeEntity destination, Derivative source) {
        AbstractInstrumentEntity destBaseInstrument = null;
        final var sourceBaseInstrument = source.getBaseInstrument();
        if (sourceBaseInstrument != null) {
            final var sourceBaseInstrumentType = sourceBaseInstrument.getType();
            if (sourceBaseInstrumentType != null) {
                final var sourceBaseInstrumentTypeRootCode = sourceBaseInstrumentType.getRootType() != null
                        ? sourceBaseInstrumentType.getRootType().getCode()
                        : "UNKNOWN";
                switch (sourceBaseInstrumentTypeRootCode) {
                    case "SEC":
                        destBaseInstrument = this.securitiesRepository.findByGuid(sourceBaseInstrument.getGuid()).orElse(null);
                        break;
                    case "CUR":
                        destBaseInstrument = this.currenciesRepository.findByGuid(sourceBaseInstrument.getGuid()).orElse(null);
                        break;
                    case "DER":
                        destBaseInstrument = this.derivativesRepository.findByGuid(sourceBaseInstrument.getGuid()).orElse(null);
                        break;
                }
            }
        }

        destination
                .setExpireDate(source.getExpireDate())
                .setBaseInstrument(destBaseInstrument)
                .setInternalFullName(source.getInternalFullName())
                .setInternalShortName(source.getInternalShortName())
                .setType(InstrumentTypeEntityFromDtoConverter.getEntityByDto(this.instrumentTypesRepository, source.getType()));
    }

    @Override
    protected DerivativeEntity getOrCreateEntityByDto(Derivative source) {
        final var result = getEntityByDto(this.derivativesRepository, source);
        return Objects.requireNonNullElseGet(result, DerivativeEntity::new);
    }

    public static DerivativeEntity getEntityByDto(DerivativesRepository entitiesRepository, Derivative source) {
        if (source == null) {
            return null;
        }
        return entitiesRepository.findByGuid(source.getGuid()).orElse(null);
    }}
