package ru.gx.fin.core.base.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gx.fin.core.base.db.entities.PlaceEntity;

import java.util.Optional;

@Repository
public interface PlacesRepository extends JpaRepository<PlaceEntity, Integer> {
    Optional<PlaceEntity> findByCode(String code);
}
