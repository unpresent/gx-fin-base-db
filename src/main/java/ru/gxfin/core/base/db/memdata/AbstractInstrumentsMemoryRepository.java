package ru.gxfin.core.base.db.memdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.gxfin.common.data.AbstractMemoryRepository;
import ru.gxfin.common.data.DataPackage;
import ru.gxfin.common.data.SingletonInstanceAlreadyExistsException;
import ru.gxfin.core.base.db.dto.AbstractInstrument;
import ru.gxfin.core.base.db.dto.AbstractInstrumentsPackage;

public abstract class AbstractInstrumentsMemoryRepository<E extends AbstractInstrument, P extends DataPackage<E>>
        extends AbstractMemoryRepository<E, P> {
    protected AbstractInstrumentsMemoryRepository(ObjectMapper objectMapper)
            throws SingletonInstanceAlreadyExistsException {
        super(objectMapper);
    }

    @Override
    public Object extractKey(E instrument) {
        return instrument.getGuid();
    }
}
