package ru.gx.fin.base.db.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(schema = "Base", name = "Instruments=Derivatives")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString
public class DerivativeEntity extends AbstractInstrumentEntity {
    /**
     * Базовый актив.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SELECT)
    @JoinColumn(name = "BaseInstrument_Id", nullable = true)
    private AbstractInstrumentEntity baseInstrument;

    /**
     * Дата экспирации.
     */
    @Column(name = "ExpireDate", nullable = true)
    private LocalDate expireDate;
}
