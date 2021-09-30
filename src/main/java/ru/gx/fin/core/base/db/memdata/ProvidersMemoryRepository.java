package ru.gx.fin.core.base.db.memdata;

import org.jetbrains.annotations.NotNull;
import ru.gx.data.AbstractMemoryRepository;
import ru.gx.fin.core.base.db.dto.Provider;
import ru.gx.fin.core.base.db.dto.ProvidersPackage;

public class ProvidersMemoryRepository extends AbstractMemoryRepository<Provider, ProvidersPackage> {
    @Override
    @NotNull
    public Object extractKey(@NotNull final Provider provider) {
        return provider.getCode();
    }

    public static class IdResolver extends AbstractIdResolver<ProvidersMemoryRepository> {
    }
}
