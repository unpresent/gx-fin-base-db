package ru.gxfin.core.base.db.entities.kafka;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.gxfin.common.data.AbstractEntityObject;

import javax.persistence.*;

@Entity
@IdClass(KafkaSnapshotPublishedOffsetEntityId.class)
@Table(schema = "Kafka", name = "SnapshotPublishedOffsets")
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString
public class KafkaSnapshotPublishedOffsetEntity extends AbstractEntityObject {
    @Id
    @Column(name = "Topic", length = 100, nullable = false)
    private String topic;

    @Column(name = "Partition", nullable = false)
    private int partition;

    @Column(name = "StartOffset", nullable = false)
    private long startOffset;
}
