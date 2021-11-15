package ru.gx.fin.base.db.converters;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gx.data.NotAllowedObjectUpdateException;
import ru.gx.data.edlinking.AbstractDtoFromEntityConverter;
import ru.gx.fin.base.db.dto.Place;
import ru.gx.fin.base.db.entities.PlaceEntity;
import ru.gx.fin.base.db.memdata.PlacesMemoryRepository;

import static lombok.AccessLevel.PROTECTED;

public class PlaceDtoFromEntityConverter extends AbstractDtoFromEntityConverter<Place, PlaceEntity> {
    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private PlacesMemoryRepository placesMemoryRepository;

    @Override
    @Nullable
    public Place findDtoBySource(@Nullable final PlaceEntity source) {
        if (source == null) {
            return null;
        }
        final var sourceCode = source.getCode();
        if (sourceCode == null) {
            return null;
        }
        return this.placesMemoryRepository.getByKey(sourceCode);
    }

    @Override
    @NotNull
    public Place createDtoBySource(@NotNull PlaceEntity source) {
        return new Place(
                source.getCode(),
                source.getName()
        );
    }

    @Override
    public boolean isDestinationUpdatable(@NotNull Place destination) {
        return false;
    }

    @Override
    public void updateDtoBySource(@NotNull Place place, @NotNull PlaceEntity placeEntity) throws NotAllowedObjectUpdateException {
        throw new NotAllowedObjectUpdateException(Place.class, null);
    }
}
