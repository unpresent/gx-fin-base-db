SELECT I."Id",
       I."Type_Id",
       I."InternalShortName",
       I."InternalFullName",
       S."CodeISIN"
FROM "Base"."Instruments" AS I
         INNER JOIN "Base"."Instruments=Securities" AS S ON S."Id" = I."Id"
WHERE EXISTS(
              SELECT 1
              FROM "Base"."Instruments:Guids" AS G
              WHERE G."Instrument_Id" = I."Id"
                AND G."Guid" = :guid
          );

SELECT I."Id",
       I."Type_Id",
       I."InternalShortName",
       I."InternalFullName",
       C."CodeAlpha2",
       C."CodeAlpha3",
       C."CodeDec",
       C."Sign",
       C."PartsName",
       C."PartsInOne"
FROM "Base"."Instruments" AS I
         INNER JOIN "Base"."Instruments=Currencies" AS C ON C."Id" = I."Id"
WHERE EXISTS(
              SELECT 1
              FROM "Base"."Instruments:Guids" AS G
              WHERE G."Instrument_Id" = I."Id"
                AND G."Guid" = :guid
          );

SELECT I."Id",
       I."Type_Id",
       I."InternalShortName",
       I."InternalFullName",
       D."BaseInstrument_Id",
       D."ExpireDate"
FROM "Base"."Instruments" AS I
         INNER JOIN "Base"."Instruments=Derivatives" AS D ON D."Id" = I."Id"
WHERE EXISTS(
              SELECT 1
              FROM "Base"."Instruments:Guids" AS G
              WHERE G."Instrument_Id" = I."Id"
                AND G."Guid" = :guid
          );