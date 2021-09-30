package ru.gx.fin.core.base.db.memdata;

import ru.gx.fin.core.base.db.dto.Derivative;
import ru.gx.fin.core.base.db.dto.DerivativesPackage;

@SuppressWarnings("unused")
public class DerivativesMemoryRepository extends AbstractInstrumentsMemoryRepository<Derivative, DerivativesPackage> {
    public static class IdResolver extends AbstractIdResolver<DerivativesMemoryRepository> {
    }
}
