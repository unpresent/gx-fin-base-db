package ru.gx.fin.base.db.converters;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gx.fin.base.db.dto.ProvidersPackage;
import ru.gx.fin.base.db.memdata.PlacesMemoryRepository;
import ru.gx.fin.base.db.memdata.ProviderTypesMemoryRepository;
import ru.gx.fin.base.db.memdata.ProvidersMemoryRepository;
import ru.gx.data.jpa.AbstractDtoFromEntityConverter;
import ru.gx.fin.base.db.entities.ProviderEntity;
import ru.gx.fin.base.db.dto.Provider;

import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

public class ProviderDtoFromEntityConverter extends AbstractDtoFromEntityConverter<Provider, ProvidersPackage, ProviderEntity> {
    @Getter
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private ProvidersMemoryRepository providersMemoryRepository;

    @Getter
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private ProviderTypesMemoryRepository providerTypesMemoryRepository;

    @Getter
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private PlacesMemoryRepository placesMemoryRepository;

    @Override
    public void fillDtoFromEntity(@NotNull final Provider destination, @NotNull final ProviderEntity source) {
        destination
                .setCode(source.getCode())
                .setType(ProviderTypeDtoFromEntityConverter.getDtoByEntity(this.providerTypesMemoryRepository, source.getType()))
                .setPlace(PlaceDtoFromEntityConverter.getDtoByEntity(this.placesMemoryRepository, source.getPlace()));
    }

    @Override
    @NotNull
    protected Provider getOrCreateDtoByEntity(@NotNull final ProviderEntity source) {
        final var result = getDtoByEntity(this.providersMemoryRepository, source);
        return Objects.requireNonNullElseGet(result, Provider::new);
    }

    @Nullable
    public static Provider getDtoByEntity(@NotNull final ProvidersMemoryRepository memoryRepository, @Nullable final ProviderEntity source) {
        if (source == null) {
            return null;
        }
        var sourceCode = source.getCode();
        if (sourceCode == null) {
            return null;
        }
        return memoryRepository.getByKey(sourceCode);
    }
}
