package ru.gx.fin.core.base.db.converters;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gx.data.InvalidDataObjectTypeException;
import ru.gx.data.jpa.AbstractDtoFromEntityConverter;
import ru.gx.fin.core.base.db.dto.InstrumentCode;
import ru.gx.fin.core.base.db.dto.SecuritiesPackage;
import ru.gx.fin.core.base.db.dto.Security;
import ru.gx.fin.core.base.db.entities.SecurityEntity;
import ru.gx.fin.core.base.db.memdata.InstrumentTypesMemoryRepository;
import ru.gx.fin.core.base.db.memdata.ProvidersMemoryRepository;
import ru.gx.fin.core.base.db.memdata.SecuritiesMemoryRepository;

import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

public class SecurityDtoFromEntityConverter extends AbstractDtoFromEntityConverter<Security, SecuritiesPackage, SecurityEntity> {
    @Getter
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private ProvidersMemoryRepository providersMemoryRepository;

    @Getter
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private SecuritiesMemoryRepository securitiesMemoryRepository;

    @Getter
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private InstrumentTypesMemoryRepository instrumentTypesMemoryRepository;

    @Override
    public void fillDtoFromEntity(@NotNull final Security destination, @NotNull final SecurityEntity source) {
        destination
                .setCodeIsin(source.getCodeIsin())
                .setInternalFullName(source.getInternalFullName())
                .setInternalShortName(source.getInternalShortName())
                .setType(InstrumentTypeDtoFromEntityConverter.getDtoByEntity(this.instrumentTypesMemoryRepository, source.getType()))
                .setGuid(source.getPrimaryGuid());
        source.getCodes().forEach(sourceCode -> {
            final var code = new InstrumentCode()
                    .setCode(sourceCode.getCode())
                    .setDateFrom(sourceCode.getDateFrom())
                    .setDateTo(sourceCode.getDateTo())
                    .setIndex(sourceCode.getIndex())
                    .setProvider(providersMemoryRepository.getByKey(sourceCode.getProvider().getCode()));
            destination.getCodes().add(code);
        });
    }

    @Override
    @NotNull
    protected Security getOrCreateDtoByEntity(@NotNull final SecurityEntity source) {
        final var result = getDtoByEntity(this.securitiesMemoryRepository, source);
        return Objects.requireNonNullElseGet(result, Security::new);
    }

    @Nullable
    public static Security getDtoByEntity(@NotNull final SecuritiesMemoryRepository memoryRepository, @Nullable final SecurityEntity source) {
        if (source == null) {
            return null;
        }
        return memoryRepository.getByKey(source.getPrimaryGuid());
    }
}
