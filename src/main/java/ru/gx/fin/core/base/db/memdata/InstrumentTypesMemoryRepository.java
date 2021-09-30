package ru.gx.fin.core.base.db.memdata;

import org.jetbrains.annotations.NotNull;
import ru.gx.data.AbstractMemoryRepository;
import ru.gx.fin.core.base.db.dto.InstrumentType;
import ru.gx.fin.core.base.db.dto.InstrumentTypesPackage;

public class InstrumentTypesMemoryRepository extends AbstractMemoryRepository<InstrumentType, InstrumentTypesPackage> {
    @Override
    @NotNull
    public Object extractKey(@NotNull final InstrumentType instrumentType) {
        return instrumentType.getCode();
    }

    public static class IdResolver extends AbstractIdResolver<InstrumentTypesMemoryRepository> {
    }
}
