package ru.gxfin.core.base.db.memdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.gxfin.common.data.SingletonInstanceAlreadyExistsException;
import ru.gxfin.core.base.db.dto.SecuritiesPackage;
import ru.gxfin.core.base.db.dto.Security;

public class SecuritiesMemoryRepository extends AbstractInstrumentsMemoryRepository<Security, SecuritiesPackage> {
    public SecuritiesMemoryRepository(ObjectMapper objectMapper)
            throws SingletonInstanceAlreadyExistsException {
        super(objectMapper);
    }

    public static class IdResolver extends AbstractIdResolver<SecuritiesMemoryRepository> {
    }
}
