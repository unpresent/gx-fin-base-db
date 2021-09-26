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
import ru.gxfin.core.base.db.memdata.ProviderTypesMemoryRepository;

/**
 * Тип провайдера
 */
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(property = "code", generator = ObjectIdGenerators.PropertyGenerator.class, resolver = ProviderTypesMemoryRepository.IdResolver.class)
public class ProviderType extends AbstractDataObject {
    /**
     * Родительсткий тип самого верхнего уровня
     */
    @JsonIdentityReference(alwaysAsId = true)
    private ProviderType rootType;

    /**
     * Родительсткий тип провайдера
     */
    @JsonIdentityReference(alwaysAsId = true)
    private ProviderType parent;

    /**
     * Код типа провайдера
     */
    private String code;

    /**
     * Название типа провайдера
     */
    private String name;
}
