package ru.gx.fin.base.db.converters;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gx.fin.base.db.entities.SecurityEntitiesPackage;
import ru.gx.fin.base.db.entities.SecurityEntity;
import ru.gx.fin.base.db.repository.SecuritiesRepository;
import ru.gx.data.edlinking.AbstractEntityFromDtoConverter;
import ru.gx.fin.base.db.repository.InstrumentTypesRepository;
import ru.gx.fin.base.db.dto.Security;

import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

public class SecurityEntityFromDtoConverter extends AbstractEntityFromDtoConverter<SecurityEntity, SecurityEntitiesPackage, Security> {
    @Getter
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private SecuritiesRepository securitiesRepository;

    @Getter
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private InstrumentTypesRepository instrumentTypesRepository;

    @Override
    public void fillEntityFromDto(@NotNull final SecurityEntity destination, @NotNull final Security source) {
        destination
                .setCodeIsin(source.getCodeIsin())
                .setInternalFullName(source.getInternalFullName())
                .setInternalShortName(source.getInternalShortName())
                .setType(InstrumentTypeEntityFromDtoConverter.getEntityByDto(this.instrumentTypesRepository, source.getType()));
        // TODO: Codes + Guids
    }

    @Override
    @NotNull
    protected SecurityEntity getOrCreateEntityByDto(@NotNull final Security source) {
        final var result = getEntityByDto(this.securitiesRepository, source);
        return Objects.requireNonNullElseGet(result, SecurityEntity::new);
    }

    @Nullable
    public static SecurityEntity getEntityByDto(@NotNull final SecuritiesRepository entitiesRepository, @Nullable final Security source) {
        if (source == null) {
            return null;
        }
        return entitiesRepository.findByGuid(source.getGuid()).orElse(null);
    }}
