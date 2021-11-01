package ru.gx.fin.base.db.converters;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gx.fin.base.db.repository.PlacesRepository;
import ru.gx.fin.base.db.repository.ProviderTypesRepository;
import ru.gx.fin.base.db.repository.ProvidersRepository;
import ru.gx.data.AbstractEntityFromDtoConverter;
import ru.gx.fin.base.db.entities.ProviderEntitiesPackage;
import ru.gx.fin.base.db.entities.ProviderEntity;
import ru.gx.fin.base.db.dto.Provider;

import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

public class ProviderEntityFromDtoConverter extends AbstractEntityFromDtoConverter<ProviderEntity, ProviderEntitiesPackage, Provider> {
    @Getter
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private ProvidersRepository providersRepository;

    @Getter
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private ProviderTypesRepository providerTypesRepository;

    @Getter
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private PlacesRepository placesRepository;

    @Override
    public void fillEntityFromDto(@NotNull final ProviderEntity destination, @NotNull final Provider source) {
        destination
                .setCode(source.getCode())
                .setType(ProviderTypeEntityFromDtoConverter.getEntityByDto(this.providerTypesRepository, source.getType()))
                .setPlace(PlaceEntityFromDtoConverter.getEntityByDto(this.placesRepository, source.getPlace()));
    }

    @Override
    @NotNull
    protected ProviderEntity getOrCreateEntityByDto(@NotNull final Provider source) {
        final var result = getEntityByDto(this.providersRepository, source);
        return Objects.requireNonNullElseGet(result, ProviderEntity::new);
    }

    @Nullable
    public static ProviderEntity getEntityByDto(@NotNull final ProvidersRepository entitiesRepository, @Nullable final Provider source) {
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
