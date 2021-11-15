package ru.gx.fin.base.db.converters;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gx.data.NotAllowedObjectUpdateException;
import ru.gx.data.edlinking.AbstractDtoFromEntityConverter;
import ru.gx.fin.base.db.dto.Security;
import ru.gx.fin.base.db.entities.SecurityEntity;
import ru.gx.fin.base.db.memdata.SecuritiesMemoryRepository;

import static lombok.AccessLevel.PROTECTED;

public class SecurityDtoFromEntityConverter extends AbstractDtoFromEntityConverter<Security, SecurityEntity> {

    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private SecuritiesMemoryRepository securitiesMemoryRepository;

    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private ProviderDtoFromEntityConverter providerDtoFromEntityConverter;

    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private InstrumentTypeDtoFromEntityConverter instrumentTypeDtoFromEntityConverter;

    @Override
    @Nullable
    public Security findDtoBySource(@Nullable SecurityEntity source) {
        if (source == null) {
            return null;
        }
        final var guid = source.getPrimaryGuid();
        if (guid == null) {
            return null;
        }
        return this.securitiesMemoryRepository.getByKey(guid);
    }

    @SneakyThrows(Exception.class)
    @Override
    public @NotNull Security createDtoBySource(@NotNull SecurityEntity source) {
        final var sourceType = source.getType();
        if (sourceType == null) {
            throw new Exception("It isn't allowed create Security with null type; source = " + source);
        }
        final var type = this.instrumentTypeDtoFromEntityConverter.findDtoBySource(source.getType());
        if (type == null) {
            throw new Exception("Can't find in memory InstrumentType by InstrumentTypeEntity; sourceType = " + sourceType);
        }

        final var result = new Security(
                source.getPrimaryGuid(),
                type,
                source.getInternalShortName(),
                source.getInternalFullName(),
                source.getCodeIsin()
        );

        CodesFillUtils.fillDtoCodes(result, source, this.providerDtoFromEntityConverter);

        return result;
    }

    @Override
    public boolean isDestinationUpdatable(@NotNull Security destination) {
        return false;
    }

    @Override
    public void updateDtoBySource(@NotNull Security destination, @NotNull SecurityEntity source) throws NotAllowedObjectUpdateException {
        throw new NotAllowedObjectUpdateException(Security.class, null);
    }
}
