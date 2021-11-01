package ru.gx.fin.base.db.converters;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gx.fin.base.db.dto.ProviderType;
import ru.gx.fin.base.db.dto.ProviderTypesPackage;
import ru.gx.fin.base.db.entities.ProviderTypeEntity;
import ru.gx.fin.base.db.memdata.ProviderTypesMemoryRepository;
import ru.gx.data.AbstractDtoFromEntityConverter;

import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

public class ProviderTypeDtoFromEntityConverter extends AbstractDtoFromEntityConverter<ProviderType, ProviderTypesPackage, ProviderTypeEntity> {
    @Getter
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private ProviderTypesMemoryRepository providerTypesMemoryRepository;

    @Override
    public void fillDtoFromEntity(@NotNull final ProviderType destination, @NotNull final ProviderTypeEntity source) {
        destination
                .setCode(source.getCode())
                .setName(source.getName());
    }

    @Override
    @NotNull
    protected ProviderType getOrCreateDtoByEntity(@NotNull final ProviderTypeEntity source) {
        final var result = getDtoByEntity(this.providerTypesMemoryRepository, source);
        return Objects.requireNonNullElseGet(result, ProviderType::new);
    }

    @Nullable
    public static ProviderType getDtoByEntity(@NotNull final ProviderTypesMemoryRepository memoryRepository, @Nullable final ProviderTypeEntity source) {
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
