package ru.gx.fin.core.base.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gx.fin.core.base.db.entities.RootObjectKindEntity;

public interface RootObjectTypesRepository extends JpaRepository<RootObjectKindEntity, String> {
}
