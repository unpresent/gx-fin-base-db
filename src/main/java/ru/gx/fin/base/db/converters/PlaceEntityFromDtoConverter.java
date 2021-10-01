package ru.gx.fin.base.db.converters;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gx.fin.base.db.entities.PlaceEntitiesPackage;
import ru.gx.fin.base.db.entities.PlaceEntity;
import ru.gx.fin.base.db.repository.PlacesRepository;
import ru.gx.data.jpa.AbstractEntityFromDtoConverter;
import ru.gx.fin.base.db.dto.Place;

import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

public class PlaceEntityFromDtoConverter extends AbstractEntityFromDtoConverter<PlaceEntity, PlaceEntitiesPackage, Place> {
    @Getter
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private PlacesRepository placesRepository;

    @Override
    public void fillEntityFromDto(@NotNull final PlaceEntity destination, @NotNull final Place source) {
        destination
                .setCode(source.getCode())
                .setName(source.getName());
    }

    @Override
    @NotNull
    protected PlaceEntity getOrCreateEntityByDto(@NotNull final Place source) {
        final var result = getEntityByDto(this.placesRepository, source);
        return Objects.requireNonNullElseGet(result, PlaceEntity::new);
    }

    @Nullable
    public static PlaceEntity getEntityByDto(@NotNull final PlacesRepository entitiesRepository, @Nullable final Place source) {
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
