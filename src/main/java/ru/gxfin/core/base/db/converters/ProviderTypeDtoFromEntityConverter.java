package ru.gxfin.core.base.db.converters;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gxfin.common.data.AbstractDtoFromEntityConverter;
import ru.gxfin.common.data.AbstractEntityFromDtoConverter;
import ru.gxfin.core.base.db.entities.PlaceEntity;
import ru.gxfin.core.base.db.dto.Place;
import ru.gxfin.core.base.db.dto.ProviderType;
import ru.gxfin.core.base.db.entities.ProviderTypeEntitiesPackage;
import ru.gxfin.core.base.db.entities.ProviderTypeEntity;
import ru.gxfin.core.base.db.dto.ProviderTypesPackage;
import ru.gxfin.core.base.db.memdata.PlacesMemoryRepository;
import ru.gxfin.core.base.db.memdata.ProviderTypesMemoryRepository;

import java.util.Objects;

public class ProviderTypeDtoFromEntityConverter extends AbstractDtoFromEntityConverter<ProviderType, ProviderTypesPackage, ProviderTypeEntity> {
    @Autowired
    private ProviderTypesMemoryRepository providerTypesMemoryRepository;

    @Override
    public void fillDtoFromEntity(ProviderType destination, ProviderTypeEntity source) {
        destination
                .setCode(source.getCode())
                .setName(source.getName());
    }

    @Override
    protected ProviderType getOrCreateDtoByEntity(@NotNull ProviderTypeEntity source) {
        final var result = getDtoByEntity(this.providerTypesMemoryRepository, source);
        return Objects.requireNonNullElseGet(result, ProviderType::new);
    }

    public static ProviderType getDtoByEntity(ProviderTypesMemoryRepository memoryRepository, ProviderTypeEntity source) {
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
