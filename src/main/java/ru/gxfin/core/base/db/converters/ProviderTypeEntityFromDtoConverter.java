package ru.gxfin.core.base.db.converters;

import org.springframework.beans.factory.annotation.Autowired;
import ru.gxfin.common.data.AbstractEntityFromDtoConverter;
import ru.gxfin.core.base.db.entities.ProviderTypeEntitiesPackage;
import ru.gxfin.core.base.db.entities.ProviderTypeEntity;
import ru.gxfin.core.base.db.repository.ProviderTypesRepository;
import ru.gxfin.core.base.db.dto.ProviderType;

import java.util.Objects;

public class ProviderTypeEntityFromDtoConverter extends AbstractEntityFromDtoConverter<ProviderTypeEntity, ProviderTypeEntitiesPackage, ProviderType> {
    @Autowired
    private ProviderTypesRepository providerTypesRepository;

    @Override
    public void fillEntityFromDto(ProviderTypeEntity destination, ProviderType source) {
        destination
                .setCode(source.getCode());
    }

    @Override
    protected ProviderTypeEntity getOrCreateEntityByDto(ProviderType source) {
        final var result = getEntityByDto(this.providerTypesRepository, source);
        return Objects.requireNonNullElseGet(result, ProviderTypeEntity::new);
    }

    public static ProviderTypeEntity getEntityByDto(ProviderTypesRepository entitiesRepository, ProviderType source) {
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
