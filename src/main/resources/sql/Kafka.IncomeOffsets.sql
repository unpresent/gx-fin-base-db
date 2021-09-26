-- Table: Kafka.IncomeOffsets
-- DROP TABLE "Kafka"."IncomeOffsets";

CREATE TABLE IF NOT EXISTS "Kafka"."IncomeOffsets"
(
    "Reader"            character varying(100) COLLATE pg_catalog."default" NOT NULL,
    "Topic"             character varying(100) COLLATE pg_catalog."default" NOT NULL,
    "Partition"         integer                                             NOT NULL,
    "Offset"            bigint                                                  NULL,
    CONSTRAINT "IncomeOffsets_pkey" PRIMARY KEY ("Reader", "Topic", "Partition")
) TABLESPACE pg_default;

ALTER TABLE "Kafka"."IncomeOffsets" OWNER TO gxfin;
