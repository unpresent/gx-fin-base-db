package ru.gx.fin.base.db.memdata;

import ru.gx.fin.base.db.dto.SecuritiesPackage;
import ru.gx.fin.base.db.dto.Security;

public class SecuritiesMemoryRepository extends AbstractInstrumentsMemoryRepository<Security, SecuritiesPackage> {
    public static class IdResolver extends AbstractIdResolver<SecuritiesMemoryRepository> {
    }
}
