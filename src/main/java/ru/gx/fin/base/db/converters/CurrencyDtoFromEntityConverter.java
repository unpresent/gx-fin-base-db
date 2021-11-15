package ru.gx.fin.base.db.converters;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gx.data.NotAllowedObjectUpdateException;
import ru.gx.data.edlinking.AbstractDtoFromEntityConverter;
import ru.gx.fin.base.db.dto.Currency;
import ru.gx.fin.base.db.entities.CurrencyEntity;
import ru.gx.fin.base.db.memdata.CurrenciesMemoryRepository;

import static lombok.AccessLevel.PROTECTED;

public class CurrencyDtoFromEntityConverter extends AbstractDtoFromEntityConverter<Currency, CurrencyEntity> {
    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    @NotNull
    private CurrenciesMemoryRepository currenciesMemoryRepository;

    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    @NotNull
    private ProviderDtoFromEntityConverter providerDtoFromEntityConverter;

    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    @NotNull
    private InstrumentTypeDtoFromEntityConverter instrumentTypeDtoFromEntityConverter;

    @Override
    @Nullable
    public Currency findDtoBySource(@Nullable final CurrencyEntity source) {
        if (source == null) {
            return null;
        }
        final var guid = source.getPrimaryGuid();
        if (guid == null) {
            return null;
        }
        return this.currenciesMemoryRepository.getByKey(guid);
    }

    @SneakyThrows(Exception.class)
    @Override
    @NotNull
    public Currency createDtoBySource(@NotNull CurrencyEntity source) {
        final var sourceType = source.getType();
        if (sourceType == null) {
            throw new Exception("It isn't allowed create Currency with null type; source = " + source);
        }
        final var type = this.instrumentTypeDtoFromEntityConverter.findDtoBySource(source.getType());
        if (type == null) {
            throw new Exception("Can't find in memory InstrumentType by InstrumentTypeEntity; sourceType = " + sourceType);
        }

        final var result = new Currency(
            source.getPrimaryGuid(),
            type,
            source.getInternalShortName(),
            source.getInternalFullName(),
            source.getCodeAlpha2(),
            source.getCodeAlpha3(),
            source.getCodeDec(),
            source.getSign(),
            source.getPartsNames(),
            source.getPartsInOne()
        );

        CodesFillUtils.fillDtoCodes(result, source, this.providerDtoFromEntityConverter);

        return result;
    }

    @Override
    public boolean isDestinationUpdatable(@NotNull Currency currency) {
        return false;
    }

    @Override
    public void updateDtoBySource(@NotNull Currency currency, @NotNull CurrencyEntity currencyEntity) throws NotAllowedObjectUpdateException {
        throw new NotAllowedObjectUpdateException(Currency.class, null);
    }
}
