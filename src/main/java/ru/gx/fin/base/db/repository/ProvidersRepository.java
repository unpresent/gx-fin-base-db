package ru.gx.fin.base.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gx.fin.base.db.entities.ProviderEntity;

import java.util.Optional;

@Repository
public interface ProvidersRepository extends JpaRepository<ProviderEntity, Short> {
    Optional<ProviderEntity> findByCode(String code);
}
