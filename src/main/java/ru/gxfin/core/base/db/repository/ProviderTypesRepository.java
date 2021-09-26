package ru.gxfin.core.base.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gxfin.core.base.db.entities.ProviderEntity;
import ru.gxfin.core.base.db.entities.ProviderTypeEntity;
import ru.gxfin.core.base.db.dto.ProviderType;

import java.util.Optional;

@Repository
public interface ProviderTypesRepository extends JpaRepository<ProviderTypeEntity, Short> {
    Optional<ProviderTypeEntity> findByCode(String code);
}
