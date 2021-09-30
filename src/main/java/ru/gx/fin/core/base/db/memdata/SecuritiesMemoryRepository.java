package ru.gx.fin.core.base.db.memdata;

import ru.gx.fin.core.base.db.dto.SecuritiesPackage;
import ru.gx.fin.core.base.db.dto.Security;

public class SecuritiesMemoryRepository extends AbstractInstrumentsMemoryRepository<Security, SecuritiesPackage> {
    public static class IdResolver extends AbstractIdResolver<SecuritiesMemoryRepository> {
    }
}
