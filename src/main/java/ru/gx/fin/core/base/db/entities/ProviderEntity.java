package ru.gx.fin.core.base.db.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ru.gx.data.jpa.AbstractEntityObject;

import javax.persistence.*;

/**
 * Провайдеры
 */
@Entity
@Table(schema = "Base", name = "Providers")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString
public class ProviderEntity extends AbstractEntityObject {
    /**
     * Идентификатор.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private short id;

    /**
     * Родительский тип провайдера
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SELECT)
    @JoinColumn(name = "Parent_Id", nullable = false)
    private ProviderEntity parent;

    /**
     * Тип провайдера
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SELECT)
    @JoinColumn(name = "Type_Id", nullable = false)
    private ProviderTypeEntity type;

    /**
     * Код.
     */
    @Column(name = "Code", length = 50, nullable = false, unique = true)
    private String code;

    /**
     * Название типа провайдера
     */
    @Column(name = "Name", length = 250, nullable = false)
    private String name;

    /**
     * Площадка, о которой транслирует данный провайдер.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SELECT)
    @JoinColumn(name = "Place_Id", nullable = true)
    private PlaceEntity place;
}
