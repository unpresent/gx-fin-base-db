package ru.gxfin.core.base.db.memdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.gxfin.common.data.AbstractMemoryRepository;
import ru.gxfin.common.data.SingletonInstanceAlreadyExistsException;
import ru.gxfin.core.base.db.dto.InstrumentType;
import ru.gxfin.core.base.db.dto.InstrumentTypesPackage;

public class InstrumentTypesMemoryRepository extends AbstractMemoryRepository<InstrumentType, InstrumentTypesPackage> {
    public InstrumentTypesMemoryRepository(ObjectMapper objectMapper)
            throws SingletonInstanceAlreadyExistsException {
        super(objectMapper);
    }

    @Override
    public Object extractKey(InstrumentType instrumentType) {
        return instrumentType.getCode();
    }

    public static class IdResolver extends AbstractIdResolver<InstrumentTypesMemoryRepository> {
    }
}
