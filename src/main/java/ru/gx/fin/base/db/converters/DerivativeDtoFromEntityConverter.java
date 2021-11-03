package ru.gx.fin.base.db.converters;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gx.data.edlinking.AbstractDtoFromEntityConverter;
import ru.gx.fin.base.db.dto.AbstractInstrument;
import ru.gx.fin.base.db.dto.DerivativesPackage;
import ru.gx.fin.base.db.dto.InstrumentCode;
import ru.gx.fin.base.db.entities.DerivativeEntity;
import ru.gx.fin.base.db.memdata.*;
import ru.gx.fin.base.db.dto.Derivative;
import ru.gx.fin.base.db.memdata.*;

import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

public class DerivativeDtoFromEntityConverter extends AbstractDtoFromEntityConverter<Derivative, DerivativesPackage, DerivativeEntity> {
    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    @NotNull
    private ProvidersMemoryRepository providersMemoryRepository;

    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    @NotNull
    private InstrumentTypesMemoryRepository instrumentTypesMemoryRepository;

    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    @NotNull
    private DerivativesMemoryRepository derivativesMemoryRepository;

    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    @NotNull
    private CurrenciesMemoryRepository currenciesMemoryRepository;

    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    @NotNull
    private SecuritiesMemoryRepository securitiesMemoryRepository;

    @Override
    public void fillDtoFromEntity(@NotNull Derivative destination, @NotNull final DerivativeEntity source) {
        final var destType = InstrumentTypeDtoFromEntityConverter.getDtoByEntity(this.instrumentTypesMemoryRepository, source.getType());

        AbstractInstrument destBaseInstrument = null;
        final var sourceBaseInstrument = source.getBaseInstrument();
        if (sourceBaseInstrument != null) {
            final var sourceBaseInstrumentType = sourceBaseInstrument.getType();
            if (sourceBaseInstrumentType != null) {
                final var sourceBaseInstrumentTypeRootCode = sourceBaseInstrumentType.getRootType() != null
                        ? sourceBaseInstrumentType.getRootType().getCode()
                        : "UNKNOWN";
                switch (sourceBaseInstrumentTypeRootCode) {
                    case "CUR":
                        destBaseInstrument = this.currenciesMemoryRepository.getByKey(sourceBaseInstrument.getPrimaryGuid());
                        break;
                    case "SEC":
                        destBaseInstrument = this.securitiesMemoryRepository.getByKey(sourceBaseInstrument.getPrimaryGuid());
                        break;
                    case "DER":
                        destBaseInstrument = this.derivativesMemoryRepository.getByKey(sourceBaseInstrument.getPrimaryGuid());
                        break;
                }
            }
        }

        destination
                .setExpireDate(source.getExpireDate())
                .setBaseInstrument(destBaseInstrument)
                .setInternalFullName(source.getInternalFullName())
                .setInternalShortName(source.getInternalShortName())
                .setType(destType)
                .setGuid(source.getPrimaryGuid());
        source.getCodes().forEach(sourceCode -> {
            final var code = new InstrumentCode()
                    .setCode(sourceCode.getCode())
                    .setDateFrom(sourceCode.getDateFrom())
                    .setDateTo(sourceCode.getDateTo())
                    .setIndex(sourceCode.getIndex())
                    .setProvider(providersMemoryRepository.getByKey(sourceCode.getProvider().getCode()));
            destination.getCodes().add(code);
        });
    }

    @Override
    @NotNull
    protected Derivative getOrCreateDtoByEntity(@NotNull final DerivativeEntity source) {
        final var result = getDtoByEntity(this.derivativesMemoryRepository, source);
        return Objects.requireNonNullElseGet(result, Derivative::new);
    }

    public static Derivative getDtoByEntity(DerivativesMemoryRepository memoryRepository, DerivativeEntity source) {
        if (source == null) {
            return null;
        }
        return memoryRepository.getByKey(source.getPrimaryGuid());
    }
}
