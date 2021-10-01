package ru.gx.fin.base.db.converters;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gx.fin.base.db.entities.InstrumentTypeEntitiesPackage;
import ru.gx.fin.base.db.entities.InstrumentTypeEntity;
import ru.gx.data.jpa.AbstractEntityFromDtoConverter;
import ru.gx.fin.base.db.repository.InstrumentTypesRepository;
import ru.gx.fin.base.db.dto.InstrumentType;

import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

public class InstrumentTypeEntityFromDtoConverter extends AbstractEntityFromDtoConverter<InstrumentTypeEntity, InstrumentTypeEntitiesPackage, InstrumentType> {
    @Getter
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private InstrumentTypesRepository instrumentTypesRepository;

    private static InstrumentTypeEntity unknownTypeEntity;

    @Override
    public void fillEntityFromDto(@NotNull final InstrumentTypeEntity destination, @NotNull final InstrumentType source) {
        destination
                .setCode(source.getCode())
                .setParent(
                        source.getParent() == null
                                ? null
                                : getEntityByDto(instrumentTypesRepository, source.getParent())
                )
                .setRootType(
                        source.getParent() == null && source.getRootType() == source
                                ? destination
                                : getEntityByDto(instrumentTypesRepository, source.getParent())
                )
                .setNameShort(source.getNameShort())
                .setNameFull(source.getNameFull());
    }

    @Override
    @NotNull
    protected InstrumentTypeEntity getOrCreateEntityByDto(@NotNull final InstrumentType source) {
        final var result = getEntityByDto(this.instrumentTypesRepository, source);
        return Objects.requireNonNullElseGet(result, InstrumentTypeEntity::new);
    }

    @Nullable
    public static InstrumentTypeEntity getEntityByDto(@NotNull final InstrumentTypesRepository entitiesRepository, @Nullable final InstrumentType source) {
        if (unknownTypeEntity == null) {
            final var res = entitiesRepository.findByCode("UNKNOWN");
            if (res.isEmpty()) {
                unknownTypeEntity = new InstrumentTypeEntity();
                unknownTypeEntity
                        .setRootType(unknownTypeEntity)
                        .setCode("UNKNOWN");
            } else {
                unknownTypeEntity = res.get();
            }
        }

        if (source == null) {
            return null;
        }
        final var sourceCode = source.getCode();
        if (sourceCode == null) {
            return null;
        }
        return entitiesRepository.findByCode(source.getCode()).orElse(unknownTypeEntity);
    }
}
