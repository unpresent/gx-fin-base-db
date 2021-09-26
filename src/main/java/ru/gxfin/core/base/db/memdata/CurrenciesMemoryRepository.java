package ru.gxfin.core.base.db.memdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.gxfin.common.data.SingletonInstanceAlreadyExistsException;
import ru.gxfin.core.base.db.dto.CurrenciesPackage;
import ru.gxfin.core.base.db.dto.Currency;

public class CurrenciesMemoryRepository extends AbstractInstrumentsMemoryRepository<Currency, CurrenciesPackage> {
    public CurrenciesMemoryRepository(ObjectMapper objectMapper)
            throws SingletonInstanceAlreadyExistsException {
        super(objectMapper);
    }

    public static class IdResolver extends AbstractIdResolver<CurrenciesMemoryRepository> {
    }
}
