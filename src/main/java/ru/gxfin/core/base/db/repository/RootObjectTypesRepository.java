package ru.gxfin.core.base.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gxfin.core.base.db.entities.RootObjectKindEntity;

public interface RootObjectTypesRepository extends JpaRepository<RootObjectKindEntity, String> {
}
