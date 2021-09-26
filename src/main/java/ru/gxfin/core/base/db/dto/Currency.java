package ru.gxfin.core.base.db.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.gxfin.core.base.db.memdata.AbstractInstrumentsMemoryRepository;
import ru.gxfin.core.base.db.memdata.CurrenciesMemoryRepository;

/**
 * Валюта
 */
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(property = "guid", generator = ObjectIdGenerators.PropertyGenerator.class, resolver = CurrenciesMemoryRepository.IdResolver.class)
public class Currency extends AbstractInstrument {

    /**
     * Код Alpha-2
     */
    private String codeAlpha2;

    /**
     * Код Alpha-3
     */
    private String codeAlpha3;

    /**
     * Код Dec
     */
    private short codeDec;

    /**
     * Знак
     */
    private String sign;

    /**
     * Название разменной части
     */
    private String partsNames;

    /**
     * Сколько разменных единиц в единице валюты (сколько "копеек в рубле")
     */
    private int partsInOne;
}
