package ru.gx.fin.base.db.converters;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gx.data.edlinking.AbstractEntityFromDtoConverter;
import ru.gx.fin.base.db.dto.Security;
import ru.gx.fin.base.db.entities.SecurityEntity;
import ru.gx.fin.base.db.repository.SecuritiesRepository;

import static lombok.AccessLevel.PROTECTED;

public class SecurityEntityFromDtoConverter extends AbstractEntityFromDtoConverter<SecurityEntity, Security> {
    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private SecuritiesRepository securitiesRepository;

    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private InstrumentTypeEntityFromDtoConverter instrumentTypeEntityFromDtoConverter;

    @Getter(PROTECTED)
    @Setter(value = PROTECTED, onMethod_ = @Autowired)
    private ProviderEntityFromDtoConverter providerEntityFromDtoConverter;

    @Override
    public @Nullable SecurityEntity findDtoBySource(@Nullable Security source) {
        if (source == null) {
            return null;
        }
        return this.securitiesRepository.findByGuid(source.getGuid()).orElse(null);
    }

    @Override
    public @NotNull SecurityEntity createDtoBySource(@NotNull Security source) {
        final var result = new SecurityEntity();
        updateDtoBySource(result, source);
        return result;
    }

    @Override
    public boolean isDestinationUpdatable(@NotNull SecurityEntity destination) {
        return true;
    }

    @SneakyThrows(Exception.class)
    @Override
    public void updateDtoBySource(@NotNull SecurityEntity destination, @NotNull Security source) {
        final var type = this.instrumentTypeEntityFromDtoConverter.findDtoBySource(source.getType());
        destination
                .setCodeIsin(source.getCodeIsin())
                .setType(type)
                .setInternalShortName(source.getInternalShortName())
                .setInternalFullName(source.getInternalFullName());

        CodesFillUtils.fillEntityCodes(destination, source, this.providerEntityFromDtoConverter);

    }
}
