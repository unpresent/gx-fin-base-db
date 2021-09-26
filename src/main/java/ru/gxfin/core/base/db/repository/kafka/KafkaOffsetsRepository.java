package ru.gxfin.core.base.db.repository.kafka;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gxfin.core.base.db.entities.kafka.KafkaIncomeOffsetEntity;
import ru.gxfin.core.base.db.entities.kafka.KafkaIncomeOffsetEntityId;

@Repository
public interface KafkaOffsetsRepository extends JpaRepository<KafkaIncomeOffsetEntity, KafkaIncomeOffsetEntityId> {
}
