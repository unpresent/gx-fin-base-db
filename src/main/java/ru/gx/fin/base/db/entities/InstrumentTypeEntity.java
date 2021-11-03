package ru.gx.fin.base.db.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ru.gx.data.entity.AbstractEntityObject;

import javax.persistence.*;

/**
 * Тип ФИ
 */
@Entity
@Table(schema = "Base", name = "Instruments_Types")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString
public class InstrumentTypeEntity extends AbstractEntityObject {
    @Id
    @Column(name = "Id", nullable = false)
    private short id;

    /**
     * Родительский тип самого верхнего уровня
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SELECT)
    @JoinColumn(name = "RootType_Id")
    private InstrumentTypeEntity rootType;

    /**
     * Родительский тип ФИ
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SELECT)
    @JoinColumn(name = "Parent_Id")
    private InstrumentTypeEntity parent;

    /**
     * Код ФИ
     */
    @Column(name = "Code", length = 50, nullable = false)
    private String code;

    /**
     * Краткое название типа ФИ
     */
    @Column(name = "NameShort", length = 100, nullable = false)
    private String nameShort;

    /**
     * Полное название типа ФИ
     */
    @Column(name = "NameFull", length = 500, nullable = false)
    private String nameFull;
}