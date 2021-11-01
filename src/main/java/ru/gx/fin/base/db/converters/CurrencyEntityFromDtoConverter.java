package ru.gx.fin.base.db.converters;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gx.fin.base.db.entities.CurrencyEntitiesPackage;
import ru.gx.fin.base.db.dto.Currency;
import ru.gx.fin.base.db.repository.CurrenciesRepository;
import ru.gx.data.AbstractEntityFromDtoConverter;
import ru.gx.fin.base.db.entities.CurrencyEntity;
import ru.gx.fin.base.db.repository.InstrumentTypesRepository;

import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

public class CurrencyEntityFromDtoConverter extends AbstractEntityFromDtoConverter<CurrencyEntity, CurrencyEntitiesPackage, Currency> {
    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    @NotNull
    private CurrenciesRepository currenciesRepository;

    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    @NotNull
    private InstrumentTypesRepository instrumentTypesRepository;

    @Override
    public void fillEntityFromDto(@NotNull final CurrencyEntity destination, @NotNull final Currency source) {
        destination
                .setCodeAlpha2(source.getCodeAlpha2())
                .setCodeAlpha3(source.getCodeAlpha3())
                .setCodeDec(source.getCodeDec())
                .setSign(source.getSign())
                .setPartsNames(source.getPartsNames())
                .setPartsInOne(source.getPartsInOne())
                .setInternalFullName(source.getInternalFullName())
                .setInternalShortName(source.getInternalShortName())
                .setType(InstrumentTypeEntityFromDtoConverter.getEntityByDto(instrumentTypesRepository, source.getType()));
    }

    @Override
    @NotNull
    protected CurrencyEntity getOrCreateEntityByDto(@NotNull final Currency source) {
        final var result = getEntityByDto(this.currenciesRepository, source);
        return Objects.requireNonNullElseGet(result, CurrencyEntity::new);
    }

    @Nullable
    public static CurrencyEntity getEntityByDto(@NotNull final CurrenciesRepository entitiesRepository, @Nullable final Currency source) {
        if (source == null) {
            return null;
        }
        return entitiesRepository.findByGuid(source.getGuid()).orElse(null);
    }
}
