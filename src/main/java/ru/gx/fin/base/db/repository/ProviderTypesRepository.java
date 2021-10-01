package ru.gx.fin.base.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gx.fin.base.db.entities.ProviderTypeEntity;

import java.util.Optional;

@Repository
public interface ProviderTypesRepository extends JpaRepository<ProviderTypeEntity, Short> {
    Optional<ProviderTypeEntity> findByCode(String code);
}
