package ru.gx.fin.base.db.converters;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gx.fin.base.db.entities.ProviderTypeEntitiesPackage;
import ru.gx.fin.base.db.repository.ProviderTypesRepository;
import ru.gx.data.edlinking.AbstractEntityFromDtoConverter;
import ru.gx.fin.base.db.entities.ProviderTypeEntity;
import ru.gx.fin.base.db.dto.ProviderType;

import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

public class ProviderTypeEntityFromDtoConverter extends AbstractEntityFromDtoConverter<ProviderTypeEntity, ProviderTypeEntitiesPackage, ProviderType> {
    @Getter
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private ProviderTypesRepository providerTypesRepository;

    @Override
    public void fillEntityFromDto(@NotNull final ProviderTypeEntity destination, @NotNull final ProviderType source) {
        destination
                .setCode(source.getCode());
    }

    @Override
    @NotNull
    protected ProviderTypeEntity getOrCreateEntityByDto(@NotNull final ProviderType source) {
        final var result = getEntityByDto(this.providerTypesRepository, source);
        return Objects.requireNonNullElseGet(result, ProviderTypeEntity::new);
    }

    @Nullable
    public static ProviderTypeEntity getEntityByDto(@NotNull final ProviderTypesRepository entitiesRepository, @Nullable final ProviderType source) {
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
