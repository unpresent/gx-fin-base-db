package ru.gxfin.core.base.db.converters;

import org.springframework.beans.factory.annotation.Autowired;
import ru.gxfin.common.data.AbstractEntityFromDtoConverter;
import ru.gxfin.core.base.db.entities.InstrumentTypeEntity;
import ru.gxfin.core.base.db.entities.PlaceEntitiesPackage;
import ru.gxfin.core.base.db.entities.PlaceEntity;
import ru.gxfin.core.base.db.repository.InstrumentTypesRepository;
import ru.gxfin.core.base.db.repository.PlacesRepository;
import ru.gxfin.core.base.db.dto.InstrumentType;
import ru.gxfin.core.base.db.dto.Place;

import java.util.Objects;

public class PlaceEntityFromDtoConverter extends AbstractEntityFromDtoConverter<PlaceEntity, PlaceEntitiesPackage, Place> {
    @Autowired
    private PlacesRepository placesRepository;

    @Override
    public void fillEntityFromDto(PlaceEntity destination, Place source) {
        destination
                .setCode(source.getCode())
                .setName(source.getName());
    }

    @Override
    protected PlaceEntity getOrCreateEntityByDto(Place source) {
        final var result = getEntityByDto(this.placesRepository, source);
        return Objects.requireNonNullElseGet(result, PlaceEntity::new);
    }

    public static PlaceEntity getEntityByDto(PlacesRepository entitiesRepository, Place source) {
        if (source == null) {
            return null;
        }
        final var sourceCode = source.getCode();
        if (sourceCode == null) {
            return null;
        }
        return entitiesRepository.findByCode(sourceCode).orElse(null);
    }
}
