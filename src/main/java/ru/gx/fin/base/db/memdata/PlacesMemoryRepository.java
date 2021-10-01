package ru.gx.fin.base.db.memdata;

import org.jetbrains.annotations.NotNull;
import ru.gx.data.AbstractMemoryRepository;
import ru.gx.fin.base.db.dto.PlacesPackage;
import ru.gx.fin.base.db.dto.Place;

public class PlacesMemoryRepository extends AbstractMemoryRepository<Place, PlacesPackage> {
    @Override
    @NotNull
    public Object extractKey(@NotNull final Place place) {
        return place.getCode();
    }

    public static class IdResolver extends AbstractIdResolver<PlacesMemoryRepository> {
    }
}
