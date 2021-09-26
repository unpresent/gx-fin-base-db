package ru.gxfin.core.base.db.converters;

import org.springframework.beans.factory.annotation.Autowired;
import ru.gxfin.common.data.AbstractEntityFromDtoConverter;
import ru.gxfin.core.base.db.entities.ProviderEntitiesPackage;
import ru.gxfin.core.base.db.entities.ProviderEntity;
import ru.gxfin.core.base.db.repository.PlacesRepository;
import ru.gxfin.core.base.db.repository.ProviderTypesRepository;
import ru.gxfin.core.base.db.repository.ProvidersRepository;
import ru.gxfin.core.base.db.dto.Provider;

import java.util.Objects;

public class ProviderEntityFromDtoConverter extends AbstractEntityFromDtoConverter<ProviderEntity, ProviderEntitiesPackage, Provider> {
    @Autowired
    private ProvidersRepository providersRepository;

    @Autowired
    private ProviderTypesRepository providerTypesRepository;

    @Autowired
    private PlacesRepository placesRepository;

    @Override
    public void fillEntityFromDto(ProviderEntity destination, Provider source) {
        destination
                .setCode(source.getCode())
                .setType(ProviderTypeEntityFromDtoConverter.getEntityByDto(this.providerTypesRepository, source.getType()))
                .setPlace(PlaceEntityFromDtoConverter.getEntityByDto(this.placesRepository, source.getPlace()));
    }

    @Override
    protected ProviderEntity getOrCreateEntityByDto(Provider source) {
        final var result = getEntityByDto(this.providersRepository, source);
        return Objects.requireNonNullElseGet(result, ProviderEntity::new);
    }

    public static ProviderEntity getEntityByDto(ProvidersRepository entitiesRepository, Provider source) {
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
