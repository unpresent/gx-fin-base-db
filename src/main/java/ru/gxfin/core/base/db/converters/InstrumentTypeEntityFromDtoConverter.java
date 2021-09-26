package ru.gxfin.core.base.db.converters;

import org.springframework.beans.factory.annotation.Autowired;
import ru.gxfin.common.data.AbstractEntityFromDtoConverter;
import ru.gxfin.core.base.db.entities.InstrumentTypeEntitiesPackage;
import ru.gxfin.core.base.db.entities.InstrumentTypeEntity;
import ru.gxfin.core.base.db.repository.InstrumentTypesRepository;
import ru.gxfin.core.base.db.dto.InstrumentType;

import java.util.Objects;

public class InstrumentTypeEntityFromDtoConverter extends AbstractEntityFromDtoConverter<InstrumentTypeEntity, InstrumentTypeEntitiesPackage, InstrumentType> {
    @Autowired
    private InstrumentTypesRepository instrumentTypesRepository;

    private static InstrumentTypeEntity unknownTypeEntity;

    @Override
    public void fillEntityFromDto(InstrumentTypeEntity destination, InstrumentType source) {
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
    protected InstrumentTypeEntity getOrCreateEntityByDto(InstrumentType source) {
        final var result = getEntityByDto(this.instrumentTypesRepository, source);
        return Objects.requireNonNullElseGet(result, InstrumentTypeEntity::new);
    }

    public static InstrumentTypeEntity getEntityByDto(InstrumentTypesRepository entitiesRepository, InstrumentType source) {
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
