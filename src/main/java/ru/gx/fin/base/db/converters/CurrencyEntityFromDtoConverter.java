package ru.gx.fin.base.db.converters;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gx.data.edlinking.AbstractEntityFromDtoConverter;
import ru.gx.fin.base.db.dto.Currency;
import ru.gx.fin.base.db.entities.CurrencyEntity;
import ru.gx.fin.base.db.entities.InstrumentCodeEntity;
import ru.gx.fin.base.db.repository.CurrenciesRepository;

import static lombok.AccessLevel.PROTECTED;

public class CurrencyEntityFromDtoConverter extends AbstractEntityFromDtoConverter<CurrencyEntity, Currency> {
    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    @NotNull
    private CurrenciesRepository currenciesRepository;

    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    @NotNull
    private InstrumentTypeEntityFromDtoConverter instrumentTypeEntityFromDtoConverter;

    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    @NotNull
    private ProviderEntityFromDtoConverter providerEntityFromDtoConverter;

    @Nullable
    public static CurrencyEntity getEntityByDto(@NotNull final CurrenciesRepository entitiesRepository, @Nullable final Currency source) {
        if (source == null) {
            return null;
        }
        return entitiesRepository.findByGuid(source.getGuid()).orElse(null);
    }

    @Override
    public @Nullable CurrencyEntity findDtoBySource(@Nullable Currency source) {
        if (source == null) {
            return null;
        }
        return this.currenciesRepository.findByGuid(source.getGuid()).orElse(null);
    }

    @Override
    public @NotNull CurrencyEntity createDtoBySource(@NotNull Currency source) {
        final var result = new CurrencyEntity();
        updateDtoBySource(result, source);
        return result;
    }

    @Override
    public boolean isDestinationUpdatable(@NotNull CurrencyEntity destination) {
        return true;
    }

    @SneakyThrows(Exception.class)
    @Override
    public void updateDtoBySource(@NotNull CurrencyEntity destination, @NotNull Currency source) {
        final var type = this.instrumentTypeEntityFromDtoConverter.findDtoBySource(source.getType());
        destination
                .setCodeAlpha2(source.getCodeAlpha2())
                .setCodeAlpha3(source.getCodeAlpha3())
                .setCodeDec(source.getCodeDec())
                .setSign(source.getSign())
                .setPartsNames(source.getPartsNames())
                .setPartsInOne(source.getPartsInOne())
                .setType(type)
                .setInternalShortName(source.getInternalShortName())
                .setInternalFullName(source.getInternalFullName());

        CodesFillUtils.fillEntityCodes(destination, source, this.providerEntityFromDtoConverter);
    }
}
