package ru.gx.fin.base.db.converters;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gx.data.jpa.AbstractEntityFromDtoConverter;
import ru.gx.fin.base.db.repository.SecuritiesRepository;
import ru.gx.fin.base.db.dto.Derivative;
import ru.gx.fin.base.db.entities.AbstractInstrumentEntity;
import ru.gx.fin.base.db.entities.DerivativeEntitiesPackage;
import ru.gx.fin.base.db.entities.DerivativeEntity;
import ru.gx.fin.base.db.repository.CurrenciesRepository;
import ru.gx.fin.base.db.repository.DerivativesRepository;
import ru.gx.fin.base.db.repository.InstrumentTypesRepository;

import java.util.Objects;

import static lombok.AccessLevel.*;

public class DerivativeEntityFromDtoConverter extends AbstractEntityFromDtoConverter<DerivativeEntity, DerivativeEntitiesPackage, Derivative> {
    @Getter
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private CurrenciesRepository currenciesRepository;

    @Getter
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private SecuritiesRepository securitiesRepository;

    @Getter
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private DerivativesRepository derivativesRepository;

    @Getter
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private InstrumentTypesRepository instrumentTypesRepository;

    @Override
    public void fillEntityFromDto(@NotNull final DerivativeEntity destination, @NotNull final Derivative source) {
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
    @NotNull
    protected DerivativeEntity getOrCreateEntityByDto(@NotNull final Derivative source) {
        final var result = getEntityByDto(this.derivativesRepository, source);
        return Objects.requireNonNullElseGet(result, DerivativeEntity::new);
    }

    @Nullable
    public static DerivativeEntity getEntityByDto(@NotNull final DerivativesRepository entitiesRepository, @Nullable final Derivative source) {
        if (source == null) {
            return null;
        }
        return entitiesRepository.findByGuid(source.getGuid()).orElse(null);
    }}
