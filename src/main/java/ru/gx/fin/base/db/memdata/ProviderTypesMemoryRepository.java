package ru.gx.fin.base.db.memdata;

import org.jetbrains.annotations.NotNull;
import ru.gx.data.AbstractMemoryRepository;
import ru.gx.fin.base.db.dto.ProviderTypesPackage;
import ru.gx.fin.base.db.dto.ProviderType;

public class ProviderTypesMemoryRepository extends AbstractMemoryRepository<ProviderType, ProviderTypesPackage> {
    @Override
    @NotNull
    public Object extractKey(@NotNull final ProviderType providerType) {
        return providerType.getCode();
    }

    public static class IdResolver extends AbstractIdResolver<ProviderTypesMemoryRepository> {
    }
}
