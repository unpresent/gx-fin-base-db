package ru.gx.fin.base.db.memdata;

import ru.gx.fin.base.db.dto.Derivative;
import ru.gx.fin.base.db.dto.DerivativesPackage;

@SuppressWarnings("unused")
public class DerivativesMemoryRepository extends AbstractInstrumentsMemoryRepository<Derivative, DerivativesPackage> {
    public static class IdResolver extends AbstractIdResolver<DerivativesMemoryRepository> {
    }
}
