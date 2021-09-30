package ru.gx.fin.core.base.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.gx.fin.core.base.db.entities.CurrencyEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CurrenciesRepository extends JpaRepository<CurrencyEntity, Integer> {
    @Query(value =
            "SELECT I.\"Id\",\n" +
            "       I.\"Type_Id\",\n" +
            "       I.\"InternalShortName\",\n" +
            "       I.\"InternalFullName\",\n" +
            "       C.\"CodeAlpha2\",\n" +
            "       C.\"CodeAlpha3\",\n" +
            "       C.\"CodeDec\",\n" +
            "       C.\"Sign\",\n" +
            "       C.\"PartsName\",\n" +
            "       C.\"PartsInOne\"\n" +
            "FROM \"Base\".\"Instruments\" AS I\n" +
            "         INNER JOIN \"Base\".\"Instruments=Currencies\" AS C ON C.\"Id\" = I.\"Id\"\n" +
            "WHERE EXISTS(\n" +
            "              SELECT 1\n" +
            "              FROM \"Base\".\"Instruments:Guids\" AS G\n" +
            "              WHERE G.\"Instrument_Id\" = I.\"Id\"\n" +
            "                AND G.\"Guid\" = :guid\n" +
            "          )", nativeQuery = true)
    Optional<CurrencyEntity> findByGuid(@Param("guid") UUID guid);

    Optional<CurrencyEntity> findByCodeAlpha3(String codeAlpha3);
}
