package ru.gxfin.core.base.db.dto;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.gxfin.common.data.AbstractDataObject;
import ru.gxfin.core.base.db.dto.InstrumentCode;
import ru.gxfin.core.base.db.dto.InstrumentType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * ФИ
 */
@Getter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractInstrument extends AbstractDataObject {
    @Setter
    private UUID guid;

    /**
     * Тип ФИ
     */
    @Setter
    @JsonIdentityReference(alwaysAsId = true)
    private InstrumentType type;

    /**
     * Краткое внутреннее (в наших системах) наименование ФИ
     */
    @Setter
    private String internalShortName;

    /**
     * Полное внутреннее (в наших системах) наименование ФИ
     */
    @Setter
    private String internalFullName;

    private final List<InstrumentCode> codes = new ArrayList<>();

    protected AbstractInstrument() {
        super();
    }
}
