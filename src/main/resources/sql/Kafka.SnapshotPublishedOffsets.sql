-- Table: Kafka.SnapshotPublishedOffsets
-- DROP TABLE "Kafka"."SnapshotPublishedOffsets";

CREATE TABLE IF NOT EXISTS "Kafka"."SnapshotPublishedOffsets"
(
    "Topic"             character varying(100) COLLATE pg_catalog."default" NOT NULL,
    "Partition"         integer                                             NOT NULL,
    "StartOffset"       bigint                                                  NULL,
    CONSTRAINT "SnapshotPublishedOffsets_pkey" PRIMARY KEY ("Topic", "Partition")
) TABLESPACE pg_default;

ALTER TABLE "Kafka"."SnapshotPublishedOffsets" OWNER TO gxfin;
