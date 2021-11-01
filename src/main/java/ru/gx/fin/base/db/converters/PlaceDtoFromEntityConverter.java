package ru.gx.fin.base.db.converters;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gx.fin.base.db.dto.PlacesPackage;
import ru.gx.fin.base.db.entities.PlaceEntity;
import ru.gx.fin.base.db.memdata.PlacesMemoryRepository;
import ru.gx.data.AbstractDtoFromEntityConverter;
import ru.gx.fin.base.db.dto.Place;

import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

public class PlaceDtoFromEntityConverter extends AbstractDtoFromEntityConverter<Place, PlacesPackage, PlaceEntity> {
    @Getter
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private PlacesMemoryRepository placesMemoryRepository;

    @Override
    public void fillDtoFromEntity(@NotNull final Place destination, @NotNull final PlaceEntity source) {
        destination
                .setCode(source.getCode())
                .setName(source.getName());
    }

    @Override
    @NotNull
    protected Place getOrCreateDtoByEntity(@NotNull final PlaceEntity source) {
        final var result = getDtoByEntity(this.placesMemoryRepository, source);
        return Objects.requireNonNullElseGet(result, Place::new);
    }

    @Nullable
    public static Place getDtoByEntity(@NotNull final PlacesMemoryRepository memoryRepository, @Nullable final PlaceEntity source) {
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
