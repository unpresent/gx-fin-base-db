package ru.gxfin.core.base.db.converters;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gxfin.common.data.AbstractDtoFromEntityConverter;
import ru.gxfin.core.base.db.entities.InstrumentTypeEntity;
import ru.gxfin.core.base.db.dto.InstrumentType;
import ru.gxfin.core.base.db.dto.InstrumentTypesPackage;
import ru.gxfin.core.base.db.memdata.InstrumentTypesMemoryRepository;

import java.util.Objects;

public class InstrumentTypeDtoFromEntityConverter extends AbstractDtoFromEntityConverter<InstrumentType, InstrumentTypesPackage, InstrumentTypeEntity> {
    @Autowired
    private InstrumentTypesMemoryRepository instrumentTypesMemoryRepository;

    @Override
    public void fillDtoFromEntity(InstrumentType destination, InstrumentTypeEntity source) {
        // TODO: Подумать над RootType == this
        destination
                .setCode(source.getCode())
                .setParent(
                        getDtoByEntity(this.instrumentTypesMemoryRepository, source.getParent())
                )
                .setRootType(
                        source.getParent() == null && source == source.getRootType()
                        ? null
                        : getDtoByEntity(this.instrumentTypesMemoryRepository, source.getRootType())
                )
                .setNameShort(source.getNameShort())
                .setNameFull(source.getNameFull());
    }

    @Override
    protected InstrumentType getOrCreateDtoByEntity(@NotNull InstrumentTypeEntity source) {
        final var result = getDtoByEntity(this.instrumentTypesMemoryRepository, source);
        return Objects.requireNonNullElseGet(result, InstrumentType::new);
    }

    public static InstrumentType getDtoByEntity(InstrumentTypesMemoryRepository memoryRepository, InstrumentTypeEntity source) {
        if (source == null) {
            return null;
        }
        final var sourceCode = source.getCode();
        if (sourceCode == null) {
            return null;
        }
        return memoryRepository.getByKey(sourceCode);
    }
}
