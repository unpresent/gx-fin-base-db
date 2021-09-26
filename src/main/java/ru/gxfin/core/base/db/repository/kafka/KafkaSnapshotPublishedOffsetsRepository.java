package ru.gxfin.core.base.db.repository.kafka;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gxfin.core.base.db.entities.kafka.KafkaSnapshotPublishedOffsetEntity;

@Repository
public interface KafkaSnapshotPublishedOffsetsRepository extends JpaRepository<KafkaSnapshotPublishedOffsetEntity, String> {
}
