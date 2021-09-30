package ru.gx.fin.core.base.db.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.gx.fin.core.base.db.memdata.SecuritiesMemoryRepository;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
 @JsonIdentityInfo(property = "guid", generator = ObjectIdGenerators.PropertyGenerator.class, resolver = SecuritiesMemoryRepository.IdResolver.class)
public class Security extends AbstractInstrument {

    /**
     * Код ISIN
     */
    private String codeIsin;
}
