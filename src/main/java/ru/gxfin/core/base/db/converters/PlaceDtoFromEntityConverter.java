package ru.gxfin.core.base.db.converters;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gxfin.common.data.AbstractDtoFromEntityConverter;
import ru.gxfin.core.base.db.entities.InstrumentTypeEntity;
import ru.gxfin.core.base.db.entities.PlaceEntity;
import ru.gxfin.core.base.db.dto.InstrumentType;
import ru.gxfin.core.base.db.dto.Place;
import ru.gxfin.core.base.db.dto.PlacesPackage;
import ru.gxfin.core.base.db.memdata.InstrumentTypesMemoryRepository;
import ru.gxfin.core.base.db.memdata.PlacesMemoryRepository;

import java.util.Objects;

public class PlaceDtoFromEntityConverter extends AbstractDtoFromEntityConverter<Place, PlacesPackage, PlaceEntity> {
    @Autowired
    private PlacesMemoryRepository placesMemoryRepository;

    @Override
    public void fillDtoFromEntity(Place destination, PlaceEntity source) {
        destination
                .setCode(source.getCode())
                .setName(source.getName());
    }

    @Override
    protected Place getOrCreateDtoByEntity(@NotNull PlaceEntity source) {
        final var result = getDtoByEntity(this.placesMemoryRepository, source);
        return Objects.requireNonNullElseGet(result, Place::new);
    }

    public static Place getDtoByEntity(PlacesMemoryRepository memoryRepository, PlaceEntity source) {
        if (source == null) {
            return null;
        }
        final var sourceCode = source.getCode();
        if (sourceCode == null) {
            return null;
        }
        return memoryRepository.getByKey(source.getCode());
    }
}
