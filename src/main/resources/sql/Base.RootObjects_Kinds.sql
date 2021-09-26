-- Table: Base.RootObjects_Kinds

-- DROP TABLE "Base"."RootObjects_Kinds";

CREATE TABLE IF NOT EXISTS "Base"."RootObjects_Kinds"
(
    "Id"        char            COLLATE pg_catalog."default"    NOT NULL,
    "Name"      varchar(50)     COLLATE pg_catalog."default"        NULL,
    CONSTRAINT "RootObjects_Kinds_pkey" PRIMARY KEY ("Id")
) TABLESPACE pg_default;

ALTER TABLE "Base"."RootObjects_Kinds" OWNER to gxfin;

INSERT INTO "Base"."RootObjects_Kinds" ("Id", "Name")
VALUES
    ('P', 'PERSON'),
    ('A', 'ACCOUNT')
ON CONFLICT ("Id") DO UPDATE SET
    "Name" = EXCLUDED."Name";
