package ru.gx.fin.core.base.db.memdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import ru.gx.data.AbstractMemoryRepository;
import ru.gx.data.SingletonInstanceAlreadyExistsException;
import ru.gx.fin.core.base.db.dto.Place;
import ru.gx.fin.core.base.db.dto.PlacesPackage;

public class PlacesMemoryRepository extends AbstractMemoryRepository<Place, PlacesPackage> {
    @Override
    @NotNull
    public Object extractKey(@NotNull final Place place) {
        return place.getCode();
    }

    public static class IdResolver extends AbstractIdResolver<PlacesMemoryRepository> {
    }
}
