package ru.gx.fin.base.db.memdata;

import org.jetbrains.annotations.NotNull;
import ru.gx.data.AbstractMemoryRepository;
import ru.gx.data.DataPackage;
import ru.gx.fin.base.db.dto.AbstractInstrument;

public abstract class AbstractInstrumentsMemoryRepository<E extends AbstractInstrument, P extends DataPackage<E>>
        extends AbstractMemoryRepository<E, P> {
    @Override
    @NotNull
    public Object extractKey(@NotNull final E instrument) {
        return instrument.getGuid();
    }
}
