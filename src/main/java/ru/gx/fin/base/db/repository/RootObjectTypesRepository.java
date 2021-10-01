package ru.gx.fin.base.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gx.fin.base.db.entities.RootObjectKindEntity;

public interface RootObjectTypesRepository extends JpaRepository<RootObjectKindEntity, String> {
}
