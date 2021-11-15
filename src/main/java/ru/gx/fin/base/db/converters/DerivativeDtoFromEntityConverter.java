package ru.gx.fin.base.db.converters;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gx.data.NotAllowedObjectUpdateException;
import ru.gx.data.edlinking.AbstractDtoFromEntityConverter;
import ru.gx.fin.base.db.dto.AbstractInstrument;
import ru.gx.fin.base.db.dto.Derivative;
import ru.gx.fin.base.db.entities.CurrencyEntity;
import ru.gx.fin.base.db.entities.DerivativeEntity;
import ru.gx.fin.base.db.entities.SecurityEntity;
import ru.gx.fin.base.db.memdata.DerivativesMemoryRepository;

import static lombok.AccessLevel.PROTECTED;

public class DerivativeDtoFromEntityConverter extends AbstractDtoFromEntityConverter<Derivative, DerivativeEntity> {
    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    @NotNull
    private DerivativesMemoryRepository derivativesMemoryRepository;

    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    @NotNull
    private ProviderDtoFromEntityConverter providerDtoFromEntityConverter;

    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    @NotNull
    private InstrumentTypeDtoFromEntityConverter instrumentTypeDtoFromEntityConverter;

    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    @NotNull
    private CurrencyDtoFromEntityConverter currencyDtoFromEntityConverter;

    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    @NotNull
    private SecurityDtoFromEntityConverter securityDtoFromEntityConverter;

    @Override
    public @Nullable Derivative findDtoBySource(@Nullable DerivativeEntity source) {
        if (source == null) {
            return null;
        }
        final var guid = source.getPrimaryGuid();
        if (guid == null) {
            return null;
        }
        return this.derivativesMemoryRepository.getByKey(guid);
    }

    @SneakyThrows(Exception.class)
    @Override
    public @NotNull Derivative createDtoBySource(@NotNull DerivativeEntity source) {

        final var sourceType = source.getType();
        if (sourceType == null) {
            throw new Exception("It isn't allowed create Derivative with null Type; source = " + source);
        }
        final var type = this.instrumentTypeDtoFromEntityConverter.findDtoBySource(source.getType());
        if (type == null) {
            throw new Exception("Can't find in memory InstrumentType by InstrumentTypeEntity; sourceType = " + sourceType);
        }

        AbstractInstrument destBaseInstrument = null;
        final var sourceBaseInstrument = source.getBaseInstrument();
        if (sourceBaseInstrument != null) {
            if (sourceBaseInstrument instanceof CurrencyEntity) {
                destBaseInstrument = this.currencyDtoFromEntityConverter.findDtoBySource((CurrencyEntity) sourceBaseInstrument);
            } else if (sourceBaseInstrument instanceof SecurityEntity) {
                destBaseInstrument = this.securityDtoFromEntityConverter.findDtoBySource((SecurityEntity) sourceBaseInstrument);
            } else if (sourceBaseInstrument instanceof DerivativeEntity) {
                destBaseInstrument = this.findDtoBySource((DerivativeEntity) sourceBaseInstrument);
            }
        }

        return new Derivative(
                source.getPrimaryGuid(),
                type,
                source.getInternalShortName(),
                source.getInternalFullName(),
                destBaseInstrument,
                source.getExpireDate()
        );
    }

    @Override
    public boolean isDestinationUpdatable(@NotNull Derivative destination) {
        return false;
    }

    @Override
    public void updateDtoBySource(@NotNull Derivative destination, @NotNull DerivativeEntity source) throws NotAllowedObjectUpdateException {
        throw new NotAllowedObjectUpdateException(Derivative.class, null);
    }
}
