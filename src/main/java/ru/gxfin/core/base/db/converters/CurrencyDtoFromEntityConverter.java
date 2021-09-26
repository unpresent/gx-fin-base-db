package ru.gxfin.core.base.db.converters;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gxfin.common.data.AbstractDtoFromEntityConverter;
import ru.gxfin.common.data.InvalidDataObjectTypeException;
import ru.gxfin.core.base.db.dto.CurrenciesPackage;
import ru.gxfin.core.base.db.dto.Currency;
import ru.gxfin.core.base.db.dto.InstrumentCode;
import ru.gxfin.core.base.db.entities.CurrencyEntity;
import ru.gxfin.core.base.db.memdata.CurrenciesMemoryRepository;
import ru.gxfin.core.base.db.memdata.InstrumentTypesMemoryRepository;
import ru.gxfin.core.base.db.memdata.AbstractInstrumentsMemoryRepository;
import ru.gxfin.core.base.db.memdata.ProvidersMemoryRepository;

import java.util.Objects;

public class CurrencyDtoFromEntityConverter extends AbstractDtoFromEntityConverter<Currency, CurrenciesPackage, CurrencyEntity> {
    @Autowired
    private ProvidersMemoryRepository providersMemoryRepository;

    @Autowired
    private InstrumentTypesMemoryRepository instrumentTypesMemoryRepository;

    @Autowired
    private CurrenciesMemoryRepository currenciesMemoryRepository;

    @Override
    public void fillDtoFromEntity(Currency destination, CurrencyEntity source) {
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
    protected Currency getOrCreateDtoByEntity(@NotNull CurrencyEntity source) throws InvalidDataObjectTypeException {
        final var result = getDtoByEntity(this.currenciesMemoryRepository, source);
        return Objects.requireNonNullElseGet(result, Currency::new);
    }

    public static Currency getDtoByEntity(CurrenciesMemoryRepository memoryRepository, CurrencyEntity source) {
        if (source == null) {
            return null;
        }
        return memoryRepository.getByKey(source.getPrimaryGuid());
    }

}
