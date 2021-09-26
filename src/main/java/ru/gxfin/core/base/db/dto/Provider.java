package ru.gxfin.core.base.db.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.gxfin.common.data.AbstractDataObject;
import ru.gxfin.core.base.db.dto.Place;
import ru.gxfin.core.base.db.dto.ProviderType;
import ru.gxfin.core.base.db.memdata.ProvidersMemoryRepository;

/**
 * Провайдеры
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(property = "code", generator = ObjectIdGenerators.PropertyGenerator.class, resolver = ProvidersMemoryRepository.IdResolver.class)
public class Provider extends AbstractDataObject {
    /**
     * Код
     */
    private String code;

    /**
     * Тип провайдера
     */
    @JsonIdentityReference(alwaysAsId = true)
    private ProviderType type;

    /**
     * Площадка, о которой танслирует данный провайдер
     */
    @JsonIdentityReference(alwaysAsId = true)
    private Place place;
}
