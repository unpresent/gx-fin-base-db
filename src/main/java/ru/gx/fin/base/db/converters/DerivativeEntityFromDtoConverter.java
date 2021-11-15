package ru.gx.fin.base.db.converters;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gx.data.NotAllowedObjectUpdateException;
import ru.gx.data.edlinking.AbstractEntityFromDtoConverter;
import ru.gx.fin.base.db.dto.Currency;
import ru.gx.fin.base.db.dto.Security;
import ru.gx.fin.base.db.entities.*;
import ru.gx.fin.base.db.repository.SecuritiesRepository;
import ru.gx.fin.base.db.dto.Derivative;
import ru.gx.fin.base.db.repository.CurrenciesRepository;
import ru.gx.fin.base.db.repository.DerivativesRepository;
import ru.gx.fin.base.db.repository.InstrumentTypesRepository;

import java.util.Objects;

import static lombok.AccessLevel.*;

public class DerivativeEntityFromDtoConverter extends AbstractEntityFromDtoConverter<DerivativeEntity, Derivative> {
    @Getter
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private DerivativesRepository derivativesRepository;

    @Getter
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private InstrumentTypeEntityFromDtoConverter instrumentTypeEntityFromDtoConverter;

    @Getter
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private CurrencyEntityFromDtoConverter currencyEntityFromDtoConverter;

    @Getter
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private SecurityEntityFromDtoConverter securityEntityFromDtoConverter;

    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    @NotNull
    private ProviderEntityFromDtoConverter providerEntityFromDtoConverter;

    @Override
    public @Nullable DerivativeEntity findDtoBySource(@Nullable Derivative source) {
        if (source == null) {
            return null;
        }
        return this.derivativesRepository.findByGuid(source.getGuid()).orElse(null);
    }

    @Override
    public @NotNull DerivativeEntity createDtoBySource(@NotNull Derivative source) {
        final var result = new DerivativeEntity();
        updateDtoBySource(result, source);
        return result;
    }

    @Override
    public boolean isDestinationUpdatable(@NotNull DerivativeEntity destination) {
        return true;
    }

    @SneakyThrows(Exception.class)
    @Override
    public void updateDtoBySource(@NotNull DerivativeEntity destination, @NotNull Derivative source) {
        final var type = this.instrumentTypeEntityFromDtoConverter.findDtoBySource(source.getType());
        AbstractInstrumentEntity destBaseInstrument = null;
        final var sourceBaseInstrument = source.getBaseInstrument();
        if (sourceBaseInstrument != null) {
            if (sourceBaseInstrument instanceof Currency) {
                destBaseInstrument = this.currencyEntityFromDtoConverter.findDtoBySource((Currency) sourceBaseInstrument);
            } else if (sourceBaseInstrument instanceof Security) {
                destBaseInstrument = this.securityEntityFromDtoConverter.findDtoBySource((Security) sourceBaseInstrument);
            } else if (sourceBaseInstrument instanceof Derivative) {
                destBaseInstrument = this.findDtoBySource((Derivative) sourceBaseInstrument);
            }
        }

        destination
                .setBaseInstrument(destBaseInstrument)
                .setExpireDate(source.getExpireDate())
                .setType(type)
                .setInternalShortName(source.getInternalShortName())
                .setInternalFullName(source.getInternalFullName());

        CodesFillUtils.fillEntityCodes(destination, source, this.providerEntityFromDtoConverter);
    }
}
