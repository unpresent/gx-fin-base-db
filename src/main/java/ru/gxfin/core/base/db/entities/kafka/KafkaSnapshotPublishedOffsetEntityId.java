package ru.gxfin.core.base.db.entities.kafka;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
public class KafkaSnapshotPublishedOffsetEntityId implements Serializable {
    private String topic;
    private int partition;
}
