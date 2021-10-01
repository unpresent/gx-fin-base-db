package ru.gx.fin.base.db.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.gx.fin.base.db.memdata.PlacesMemoryRepository;
import ru.gx.data.AbstractDataObject;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(property = "code", generator = ObjectIdGenerators.PropertyGenerator.class, resolver = PlacesMemoryRepository.IdResolver.class)
public class Place extends AbstractDataObject {
    /**
     * Код.
     */
    private String code;

    /**
     * Название.
     */
    private String name;
}
