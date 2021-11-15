package ru.gx.fin.base.db.converters;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gx.data.NotAllowedObjectUpdateException;
import ru.gx.fin.base.db.dto.ProvidersPackage;
import ru.gx.fin.base.db.memdata.PlacesMemoryRepository;
import ru.gx.fin.base.db.memdata.ProviderTypesMemoryRepository;
import ru.gx.fin.base.db.memdata.ProvidersMemoryRepository;
import ru.gx.data.edlinking.AbstractDtoFromEntityConverter;
import ru.gx.fin.base.db.entities.ProviderEntity;
import ru.gx.fin.base.db.dto.Provider;

import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

public class ProviderDtoFromEntityConverter extends AbstractDtoFromEntityConverter<Provider, ProviderEntity> {
    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private ProvidersMemoryRepository providersMemoryRepository;

    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private ProviderTypeDtoFromEntityConverter providerTypeDtoFromEntityConverter;

    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private PlaceDtoFromEntityConverter placeDtoFromEntityConverter;

    @Override
    @Nullable
    public Provider findDtoBySource(@Nullable final ProviderEntity source) {
        if (source == null) {
            return null;
        }
        var sourceCode = source.getCode();
        if (sourceCode == null) {
            return null;
        }
        return this.providersMemoryRepository.getByKey(sourceCode);
    }

    @SneakyThrows(Exception.class)
    @Override
    @NotNull
    public Provider createDtoBySource(@NotNull ProviderEntity source) {
        final var sourceType = source.getType();
        if (sourceType == null) {
            throw new Exception("It isn't allowed create Provider with null type; source = " + source);
        }
        final var type = this.providerTypeDtoFromEntityConverter.findDtoBySource(sourceType);
        if (type == null) {
            throw new Exception("Can't find in memory ProviderType by ProviderTypeEntity; sourceType = " + sourceType);
        }

        final var place = this.placeDtoFromEntityConverter.findDtoBySource(source.getPlace());

        return new Provider(
                source.getCode(),
                source.getName(),
                type,
                place
        );
    }

    @Override
    public boolean isDestinationUpdatable(@NotNull Provider destination) {
        return false;
    }

    @Override
    public void updateDtoBySource(@NotNull Provider destination, @NotNull ProviderEntity source) throws NotAllowedObjectUpdateException {
        throw new NotAllowedObjectUpdateException(Provider.class, null);
    }
}
