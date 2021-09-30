package ru.gx.fin.core.base.db.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.gx.data.jpa.AbstractEntityObject;

import javax.persistence.*;

@Entity
@Table(schema = "Base", name = "RootObjects")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString
public class AbstractRootObjectEntity extends AbstractEntityObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private int id;

    @ManyToOne
    @JoinColumn(name = "Kind_Id", nullable = false)
    private RootObjectKindEntity kind;
}
