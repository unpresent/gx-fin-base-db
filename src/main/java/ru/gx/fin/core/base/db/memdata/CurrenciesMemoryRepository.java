package ru.gx.fin.core.base.db.memdata;

import ru.gx.fin.core.base.db.dto.CurrenciesPackage;
import ru.gx.fin.core.base.db.dto.Currency;

public class CurrenciesMemoryRepository extends AbstractInstrumentsMemoryRepository<Currency, CurrenciesPackage> {
    public static class IdResolver extends AbstractIdResolver<CurrenciesMemoryRepository> {
    }
}
