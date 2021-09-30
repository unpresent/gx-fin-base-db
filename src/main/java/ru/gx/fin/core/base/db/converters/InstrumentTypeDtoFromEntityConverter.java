package ru.gx.fin.core.base.db.converters;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gx.fin.core.base.db.dto.InstrumentTypesPackage;
import ru.gx.fin.core.base.db.entities.InstrumentTypeEntity;
import ru.gx.fin.core.base.db.memdata.InstrumentTypesMemoryRepository;
import ru.gx.data.jpa.AbstractDtoFromEntityConverter;
import ru.gx.fin.core.base.db.dto.InstrumentType;

import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

public class InstrumentTypeDtoFromEntityConverter extends AbstractDtoFromEntityConverter<InstrumentType, InstrumentTypesPackage, InstrumentTypeEntity> {
    @Getter
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private InstrumentTypesMemoryRepository instrumentTypesMemoryRepository;

    @Override
    public void fillDtoFromEntity(@NotNull final InstrumentType destination, @NotNull final InstrumentTypeEntity source) {
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
    @NotNull
    protected InstrumentType getOrCreateDtoByEntity(@NotNull final InstrumentTypeEntity source) {
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
