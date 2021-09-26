package ru.gxfin.core.base.db.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
public class InstrumentGuidEntityId implements Serializable {
    private AbstractInstrumentEntity instrument;
    private short index;
}
