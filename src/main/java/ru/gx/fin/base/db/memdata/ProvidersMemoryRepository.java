package ru.gx.fin.base.db.memdata;

import org.jetbrains.annotations.NotNull;
import ru.gx.data.AbstractMemoryRepository;
import ru.gx.fin.base.db.dto.ProvidersPackage;
import ru.gx.fin.base.db.dto.Provider;

public class ProvidersMemoryRepository extends AbstractMemoryRepository<Provider, ProvidersPackage> {
    @Override
    @NotNull
    public Object extractKey(@NotNull final Provider provider) {
        return provider.getCode();
    }

    public static class IdResolver extends AbstractIdResolver<ProvidersMemoryRepository> {
    }
}
