package ru.gxfin.core.base.db.converters;

import org.springframework.beans.factory.annotation.Autowired;
import ru.gxfin.common.data.AbstractEntityFromDtoConverter;
import ru.gxfin.core.base.db.entities.InstrumentTypeEntity;
import ru.gxfin.core.base.db.entities.SecurityEntitiesPackage;
import ru.gxfin.core.base.db.entities.SecurityEntity;
import ru.gxfin.core.base.db.repository.InstrumentTypesRepository;
import ru.gxfin.core.base.db.dto.Security;

public class SecurityEntityFromDtoConverter extends AbstractEntityFromDtoConverter<SecurityEntity, SecurityEntitiesPackage, Security> {
    @Autowired
    private InstrumentTypesRepository instrumentTypesRepository;

    @Override
    public void fillEntityFromDto(SecurityEntity destination, Security source) {
        destination
                .setCodeIsin(source.getCodeIsin())
                .setInternalFullName(source.getInternalFullName())
                .setInternalShortName(source.getInternalShortName())
                .setType(InstrumentTypeEntityFromDtoConverter.getEntityByDto(this.instrumentTypesRepository, source.getType()));
        // TODO: Codes + Guids

    }

    @Override
    protected SecurityEntity getOrCreateEntityByDto(Security place) {
        return new SecurityEntity();
    }
}
