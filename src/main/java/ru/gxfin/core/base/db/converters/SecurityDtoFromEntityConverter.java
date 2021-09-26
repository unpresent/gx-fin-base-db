package ru.gxfin.core.base.db.converters;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gxfin.common.data.AbstractDtoFromEntityConverter;
import ru.gxfin.common.data.InvalidDataObjectTypeException;
import ru.gxfin.core.base.db.dto.InstrumentCode;
import ru.gxfin.core.base.db.dto.SecuritiesPackage;
import ru.gxfin.core.base.db.dto.Security;
import ru.gxfin.core.base.db.entities.SecurityEntity;
import ru.gxfin.core.base.db.memdata.*;

import java.util.Objects;

public class SecurityDtoFromEntityConverter extends AbstractDtoFromEntityConverter<Security, SecuritiesPackage, SecurityEntity> {
    @Autowired
    private ProvidersMemoryRepository providersMemoryRepository;

    @Autowired
    private SecuritiesMemoryRepository securitiesMemoryRepository;

    @Autowired
    private InstrumentTypesMemoryRepository instrumentTypesMemoryRepository;

    @Override
    public void fillDtoFromEntity(Security destination, SecurityEntity source) {
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
    protected Security getOrCreateDtoByEntity(@NotNull SecurityEntity source) throws InvalidDataObjectTypeException {
        final var result = getDtoByEntity(this.securitiesMemoryRepository, source);
        return Objects.requireNonNullElseGet(result, Security::new);
    }

    public static Security getDtoByEntity(SecuritiesMemoryRepository memoryRepository, SecurityEntity source) throws InvalidDataObjectTypeException {
        if (source == null) {
            return null;
        }
        return memoryRepository.getByKey(source.getPrimaryGuid());
    }
}
