package ru.gxfin.core.base.db.memdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.gxfin.common.data.AbstractMemoryRepository;
import ru.gxfin.common.data.SingletonInstanceAlreadyExistsException;
import ru.gxfin.core.base.db.dto.ProviderType;
import ru.gxfin.core.base.db.dto.ProviderTypesPackage;

public class ProviderTypesMemoryRepository extends AbstractMemoryRepository<ProviderType, ProviderTypesPackage> {
    public ProviderTypesMemoryRepository(ObjectMapper objectMapper)
            throws SingletonInstanceAlreadyExistsException {
        super(objectMapper);
    }

    @Override
    public Object extractKey(ProviderType providerType) {
        return providerType.getCode();
    }

    public static class IdResolver extends AbstractIdResolver<ProviderTypesMemoryRepository> {
    }
}
