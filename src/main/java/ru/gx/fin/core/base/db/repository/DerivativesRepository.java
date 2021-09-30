package ru.gx.fin.core.base.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.gx.fin.core.base.db.entities.DerivativeEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DerivativesRepository extends JpaRepository<DerivativeEntity, Integer> {
    @Query(value =
            "SELECT I.\"Id\",\n" +
            "       I.\"Type_Id\",\n" +
            "       I.\"InternalShortName\",\n" +
            "       I.\"InternalFullName\",\n" +
            "       D.\"BaseInstrument_Id\",\n" +
            "       D.\"ExpireDate\"\n" +
            "FROM \"Base\".\"Instruments\" AS I\n" +
            "         INNER JOIN \"Base\".\"Instruments=Derivatives\" AS D ON D.\"Id\" = I.\"Id\"\n" +
            "WHERE EXISTS(\n" +
            "              SELECT 1\n" +
            "              FROM \"Base\".\"Instruments:Guids\" AS G\n" +
            "              WHERE G.\"Instrument_Id\" = I.\"Id\"\n" +
            "                AND G.\"Guid\" = :guid\n" +
            "          )", nativeQuery = true)
    Optional<DerivativeEntity> findByGuid(@Param("guid") UUID guid);
}
