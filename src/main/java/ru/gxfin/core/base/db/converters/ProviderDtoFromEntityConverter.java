package ru.gxfin.core.base.db.converters;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gxfin.common.data.AbstractDtoFromEntityConverter;
import ru.gxfin.core.base.db.entities.InstrumentTypeEntity;
import ru.gxfin.core.base.db.entities.ProviderEntity;
import ru.gxfin.core.base.db.dto.InstrumentType;
import ru.gxfin.core.base.db.dto.Place;
import ru.gxfin.core.base.db.dto.Provider;
import ru.gxfin.core.base.db.dto.ProvidersPackage;
import ru.gxfin.core.base.db.memdata.InstrumentTypesMemoryRepository;
import ru.gxfin.core.base.db.memdata.PlacesMemoryRepository;
import ru.gxfin.core.base.db.memdata.ProviderTypesMemoryRepository;
import ru.gxfin.core.base.db.memdata.ProvidersMemoryRepository;

import java.util.Objects;

public class ProviderDtoFromEntityConverter extends AbstractDtoFromEntityConverter<Provider, ProvidersPackage, ProviderEntity> {
    @Autowired
    private ProvidersMemoryRepository providersMemoryRepository;

    @Autowired
    private ProviderTypesMemoryRepository providerTypesMemoryRepository;

    @Autowired
    private PlacesMemoryRepository placesMemoryRepository;

    @Override
    public void fillDtoFromEntity(Provider destination, ProviderEntity source) {
        destination
                .setCode(source.getCode())
                .setType(ProviderTypeDtoFromEntityConverter.getDtoByEntity(this.providerTypesMemoryRepository, source.getType()))
                .setPlace(PlaceDtoFromEntityConverter.getDtoByEntity(this.placesMemoryRepository, source.getPlace()));
    }

    @Override
    protected Provider getOrCreateDtoByEntity(@NotNull ProviderEntity source) {
        final var result = getDtoByEntity(this.providersMemoryRepository, source);
        return Objects.requireNonNullElseGet(result, Provider::new);
    }

    public static Provider getDtoByEntity(ProvidersMemoryRepository memoryRepository, ProviderEntity source) {
        if (source == null) {
            return null;
        }
        var sourceCode = source.getCode();
        if (sourceCode == null) {
            return null;
        }
        return memoryRepository.getByKey(sourceCode);
    }
}
