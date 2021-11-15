package ru.gx.fin.base.db.converters;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gx.data.NotAllowedObjectUpdateException;
import ru.gx.data.edlinking.AbstractEntityFromDtoConverter;
import ru.gx.fin.base.db.dto.Place;
import ru.gx.fin.base.db.entities.PlaceEntity;
import ru.gx.fin.base.db.repository.PlacesRepository;

import static lombok.AccessLevel.PROTECTED;

public class PlaceEntityFromDtoConverter extends AbstractEntityFromDtoConverter<PlaceEntity, Place> {
    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private PlacesRepository placesRepository;

    @Override
    @Nullable
    public PlaceEntity findDtoBySource(@Nullable Place source) {
        if (source == null) {
            return null;
        }
        return this.placesRepository.findByCode(source.getCode()).orElse(null);
    }

    @Override
    @NotNull
    public PlaceEntity createDtoBySource(@NotNull Place source) {
        final var result = new PlaceEntity();
        updateDtoBySource(result, source);
        return result;
    }

    @Override
    public boolean isDestinationUpdatable(@NotNull PlaceEntity destination) {
        return true;
    }

    @Override
    public void updateDtoBySource(@NotNull PlaceEntity destination, @NotNull Place source) {
        destination
                .setCode(source.getCode())
                .setName(source.getName());
    }
}
