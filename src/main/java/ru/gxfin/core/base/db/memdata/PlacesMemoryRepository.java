package ru.gxfin.core.base.db.memdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.gxfin.common.data.AbstractMemoryRepository;
import ru.gxfin.common.data.SingletonInstanceAlreadyExistsException;
import ru.gxfin.core.base.db.dto.Place;
import ru.gxfin.core.base.db.dto.PlacesPackage;

public class PlacesMemoryRepository extends AbstractMemoryRepository<Place, PlacesPackage> {
    public PlacesMemoryRepository(ObjectMapper objectMapper)
            throws SingletonInstanceAlreadyExistsException {
        super(objectMapper);
    }

    @Override
    public Object extractKey(Place place) {
        return place.getCode();
    }

    public static class IdResolver extends AbstractIdResolver<PlacesMemoryRepository> {
    }
}
