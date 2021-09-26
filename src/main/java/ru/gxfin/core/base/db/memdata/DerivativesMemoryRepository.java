package ru.gxfin.core.base.db.memdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.gxfin.common.data.SingletonInstanceAlreadyExistsException;
import ru.gxfin.core.base.db.dto.Derivative;
import ru.gxfin.core.base.db.dto.DerivativesPackage;

@SuppressWarnings("unused")
public class DerivativesMemoryRepository extends AbstractInstrumentsMemoryRepository<Derivative, DerivativesPackage> {
    public DerivativesMemoryRepository(ObjectMapper objectMapper)
            throws SingletonInstanceAlreadyExistsException {
        super(objectMapper);
    }

    public static class IdResolver extends AbstractIdResolver<DerivativesMemoryRepository> {
    }
}
