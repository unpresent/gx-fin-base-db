package ru.gxfin.core.base.db.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.gxfin.common.data.AbstractDataObject;

import javax.persistence.*;

/**
 * Типы Root-объектов
 * TODO: Передалать в enum?
 */
@Entity
@Table(schema = "Base", name = "RootObjects_Kinds")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString
public class RootObjectKindEntity extends AbstractDataObject {
    /**
     * Межсистемный = Внутренний = Локальный идентификатор.
     * P - person, C - contract, A - account, FI - FinInstrument.
     */
    @Id
    @Column(name = "Id", length = 1, nullable = false)
    private Character Id;

    @Column(name = "Name", length = 100, nullable = false)
    private String name;
}
