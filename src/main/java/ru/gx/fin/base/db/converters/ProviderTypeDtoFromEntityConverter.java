package ru.gx.fin.base.db.converters;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gx.data.NotAllowedObjectUpdateException;
import ru.gx.data.edlinking.AbstractDtoFromEntityConverter;
import ru.gx.fin.base.db.dto.ProviderType;
import ru.gx.fin.base.db.entities.ProviderTypeEntity;
import ru.gx.fin.base.db.memdata.ProviderTypesMemoryRepository;

import static lombok.AccessLevel.PROTECTED;

public class ProviderTypeDtoFromEntityConverter extends AbstractDtoFromEntityConverter<ProviderType, ProviderTypeEntity> {
    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private ProviderTypesMemoryRepository providerTypesMemoryRepository;

    @Override
    @Nullable
    public ProviderType findDtoBySource(@Nullable final ProviderTypeEntity source) {
        if (source == null) {
            return null;
        }
        final var sourceCode = source.getCode();
        if (sourceCode == null) {
            return null;
        }
        return this.providerTypesMemoryRepository.getByKey(sourceCode);
    }

    @Override
    @NotNull
    public ProviderType createDtoBySource(@NotNull final ProviderTypeEntity source) {
        final var rootType = this.findDtoBySource(source.getRootType());
        final var parent = this.findDtoBySource(source.getParent());
        return new ProviderType(
                rootType,
                parent,
                source.getCode(),
                source.getName()
        );
    }

    @Override
    public boolean isDestinationUpdatable(@NotNull ProviderType destination) {
        return false;
    }

    @Override
    public void updateDtoBySource(@NotNull ProviderType destination, @NotNull ProviderTypeEntity source) throws NotAllowedObjectUpdateException {
        throw new NotAllowedObjectUpdateException(ProviderType.class, null);
    }
}
