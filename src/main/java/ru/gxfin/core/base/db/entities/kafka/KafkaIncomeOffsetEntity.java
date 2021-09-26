package ru.gxfin.core.base.db.entities.kafka;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.gxfin.common.data.AbstractEntityObject;

import javax.persistence.*;

@Entity
@IdClass(KafkaIncomeOffsetEntityId.class)
@Table(schema = "Kafka", name = "IncomeOffsets")
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString
public class KafkaIncomeOffsetEntity extends AbstractEntityObject {
    @Id
    @Column(name = "Reader", length = 100, nullable = false)
    private String reader;

    @Id
    @Column(name = "Topic", length = 100, nullable = false)
    private String topic;

    @Id
    @Column(name = "Partition", nullable = false)
    private int partition;

    @Column(name = "Offset")
    private long offset;
}
