package ru.gxfin.core.base.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.gxfin.core.base.db.entities.CurrencyEntity;
import ru.gxfin.core.base.db.entities.SecurityEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SecuritiesRepository extends JpaRepository<SecurityEntity, Integer> {
    @Query(value =
            "SELECT I.\"Id\",\n" +
            "       I.\"Type_Id\",\n" +
            "       I.\"InternalShortName\",\n" +
            "       I.\"InternalFullName\",\n" +
            "       S.\"CodeISIN\"\n" +
            "FROM \"Base\".\"Instruments\" AS I\n" +
            "         INNER JOIN \"Base\".\"Instruments=Securities\" AS S ON S.\"Id\" = I.\"Id\"\n" +
            "WHERE EXISTS(\n" +
            "              SELECT 1\n" +
            "              FROM \"Base\".\"Instruments:Guids\" AS G\n" +
            "              WHERE G.\"Instrument_Id\" = I.\"Id\"\n" +
            "                AND G.\"Guid\" = :guid\n" +
            "          )", nativeQuery = true)
    Optional<CurrencyEntity> findByGuid(@Param("guid") UUID guid);
}
