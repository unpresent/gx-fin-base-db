package ru.gxfin.core.base.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gxfin.core.base.db.entities.InstrumentTypeEntity;

import java.util.Optional;

@Repository
public interface InstrumentTypesRepository extends JpaRepository<InstrumentTypeEntity, Short> {
    Optional<InstrumentTypeEntity> findByCode(String code);
}
