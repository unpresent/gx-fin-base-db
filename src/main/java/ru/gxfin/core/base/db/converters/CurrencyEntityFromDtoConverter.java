package ru.gxfin.core.base.db.converters;

import org.springframework.beans.factory.annotation.Autowired;
import ru.gxfin.common.data.AbstractEntityFromDtoConverter;
import ru.gxfin.core.base.db.entities.CurrencyEntitiesPackage;
import ru.gxfin.core.base.db.entities.CurrencyEntity;
import ru.gxfin.core.base.db.repository.CurrenciesRepository;
import ru.gxfin.core.base.db.repository.InstrumentTypesRepository;
import ru.gxfin.core.base.db.dto.Currency;

import java.util.Objects;

public class CurrencyEntityFromDtoConverter extends AbstractEntityFromDtoConverter<CurrencyEntity, CurrencyEntitiesPackage, Currency> {
    @Autowired
    private CurrenciesRepository currenciesRepository;

    @Autowired
    private InstrumentTypesRepository instrumentTypesRepository;

    @Override
    public void fillEntityFromDto(CurrencyEntity destination, Currency source) {
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
    protected CurrencyEntity getOrCreateEntityByDto(Currency source) {
        final var result = getEntityByDto(this.currenciesRepository, source);
        return Objects.requireNonNullElseGet(result, CurrencyEntity::new);
    }

    public static CurrencyEntity getEntityByDto(CurrenciesRepository entitiesRepository, Currency source) {
        if (source == null) {
            return null;
        }
        final var sourceCodeAlpha3 = source.getCodeAlpha3();
        if (sourceCodeAlpha3 == null) {
            return null;
        }
        return entitiesRepository.findByCodeAlpha3(sourceCodeAlpha3).orElse(null);
    }
}
