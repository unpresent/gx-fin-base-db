package ru.gxfin.core.base.db.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.gxfin.common.data.AbstractDataObject;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@IdClass(InstrumentGuidEntityId.class)
@Table(schema = "Base", name = "Instruments:Guids")
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString
public class InstrumentGuidEntity extends AbstractDataObject {
    @Id
    @ManyToOne
    @JoinColumn(name = "Instrument_Id", nullable = false)
    private AbstractInstrumentEntity instrument;

    /**
     * Порядковый номер Guid-а
     */
    @Id
    @Column(name = "Index", nullable = false)
    private short index;

    /**
     * Guid
     */
    @Column(name = "Guid", nullable = false)
    private UUID guid;

    public boolean isPrimary() {
        return this.index == 1;
    }
}
