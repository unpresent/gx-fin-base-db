package ru.gxfin.core.base.db.memdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.gxfin.common.data.AbstractMemoryRepository;
import ru.gxfin.common.data.SingletonInstanceAlreadyExistsException;
import ru.gxfin.core.base.db.dto.Provider;
import ru.gxfin.core.base.db.dto.ProvidersPackage;

public class ProvidersMemoryRepository extends AbstractMemoryRepository<Provider, ProvidersPackage> {
    public ProvidersMemoryRepository(ObjectMapper objectMapper)
            throws SingletonInstanceAlreadyExistsException {
        super(objectMapper);
    }

    @Override
    public Object extractKey(Provider provider) {
        return provider.getCode();
    }

    public static class IdResolver extends AbstractIdResolver<ProvidersMemoryRepository> {
    }
}
