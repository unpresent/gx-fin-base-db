package ru.gx.fin.base.db.converters;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gx.data.jpa.AbstractDtoFromEntityConverter;
import ru.gx.fin.base.db.dto.CurrenciesPackage;
import ru.gx.fin.base.db.dto.Currency;
import ru.gx.fin.base.db.entities.CurrencyEntity;
import ru.gx.fin.base.db.memdata.CurrenciesMemoryRepository;
import ru.gx.fin.base.db.memdata.InstrumentTypesMemoryRepository;
import ru.gx.fin.base.db.memdata.ProvidersMemoryRepository;
import ru.gx.fin.base.db.dto.InstrumentCode;

import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

public class CurrencyDtoFromEntityConverter extends AbstractDtoFromEntityConverter<Currency, CurrenciesPackage, CurrencyEntity> {
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
    private CurrenciesMemoryRepository currenciesMemoryRepository;

    @Override
    public void fillDtoFromEntity(@NotNull final Currency destination, @NotNull final CurrencyEntity source) {
        destination
                .setCodeAlpha2(source.getCodeAlpha2())
                .setCodeAlpha3(source.getCodeAlpha3())
                .setCodeDec(source.getCodeDec())
                .setSign(source.getSign())
                .setPartsNames(source.getPartsNames())
                .setPartsInOne(source.getPartsInOne())
                .setType(InstrumentTypeDtoFromEntityConverter.getDtoByEntity(this.instrumentTypesMemoryRepository, source.getType()))
                .setInternalFullName(source.getInternalFullName())
                .setInternalShortName(source.getInternalShortName())
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
    protected Currency getOrCreateDtoByEntity(@NotNull final CurrencyEntity source) {
        final var result = getDtoByEntity(this.currenciesMemoryRepository, source);
        return Objects.requireNonNullElseGet(result, Currency::new);
    }

    @Nullable
    public static Currency getDtoByEntity(@NotNull final CurrenciesMemoryRepository memoryRepository, @Nullable CurrencyEntity source) {
        if (source == null) {
            return null;
        }
        return memoryRepository.getByKey(source.getPrimaryGuid());
    }

}
