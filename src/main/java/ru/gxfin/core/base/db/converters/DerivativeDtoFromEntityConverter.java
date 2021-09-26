package ru.gxfin.core.base.db.converters;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gxfin.common.data.AbstractDtoFromEntityConverter;
import ru.gxfin.common.data.InvalidDataObjectTypeException;
import ru.gxfin.core.base.db.dto.*;
import ru.gxfin.core.base.db.entities.DerivativeEntity;
import ru.gxfin.core.base.db.memdata.*;

import java.util.Objects;

public class DerivativeDtoFromEntityConverter extends AbstractDtoFromEntityConverter<Derivative, DerivativesPackage, DerivativeEntity> {
    @Autowired
    private ProvidersMemoryRepository providersMemoryRepository;

    @Autowired
    private InstrumentTypesMemoryRepository instrumentTypesMemoryRepository;

    @Autowired
    private DerivativesMemoryRepository derivativesMemoryRepository;

    @Autowired
    private CurrenciesMemoryRepository currenciesMemoryRepository;

    @Autowired
    private SecuritiesMemoryRepository securitiesMemoryRepository;

    @Override
    public void fillDtoFromEntity(@NotNull Derivative destination, DerivativeEntity source) {
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
    protected Derivative getOrCreateDtoByEntity(@NotNull DerivativeEntity source) {
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