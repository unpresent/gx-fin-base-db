package ru.gx.fin.base.db.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.gx.fin.base.db.memdata.InstrumentTypesMemoryRepository;
import ru.gx.data.AbstractDataObject;

/**
 * Тип ФИ
 */
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(property = "code", generator = ObjectIdGenerators.PropertyGenerator.class, resolver = InstrumentTypesMemoryRepository.IdResolver.class)
public class InstrumentType extends AbstractDataObject {
    /**
     * Родительсткий тип самого верхнего уровня
     */
    @JsonIdentityReference(alwaysAsId = true)
    private InstrumentType rootType;

    /**
     * Родительсткий тип ФИ
     */
    @JsonIdentityReference(alwaysAsId = true)
    private InstrumentType parent;

    /**
     * Код ФИ
     */
    private String code;

    /**
     * Краткое название типа ФИ
     */
    private String nameShort;

    /**
     * Полное название типа ФИ
     */
    private String nameFull;
}
