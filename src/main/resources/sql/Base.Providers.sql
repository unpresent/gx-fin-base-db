-- Table: Base.Providers

-- DROP TABLE "Base"."Providers";

CREATE TABLE IF NOT EXISTS "Base"."Providers"
(
    "Id"            smallint                                            NOT NULL,
    "Parent_Id"     smallint                                                NULL,
    "Type_Id"       smallint                                            NOT NULL,
    "Code"          varchar(50)     COLLATE pg_catalog."default"        NOT NULL,
    "Name"          varchar(500)    COLLATE pg_catalog."default"            NULL,
    "Place_Id"      smallint                                                NULL,
    CONSTRAINT "Providers_pkey" PRIMARY KEY ("Id"),
    CONSTRAINT "Providers_fkey_Parent" FOREIGN KEY ("Parent_Id") REFERENCES "Base"."Providers" ("Id"),
    CONSTRAINT "Providers_fkey_Type" FOREIGN KEY ("Type_Id") REFERENCES "Base"."Providers_Types" ("Id"),
    CONSTRAINT "Providers_fkey_Place" FOREIGN KEY ("Place_Id") REFERENCES "Base"."Places" ("Id")
) TABLESPACE pg_default;

ALTER TABLE "Base"."Providers" OWNER to gxfin;

INSERT INTO "Base"."Providers" ("Id", "Parent_Id", "Type_Id", "Code", "Name", "Place_Id")
VALUES
    (1,     NULL,   1,  'MOEX',         'МБ',               1),
    (2,     1,      1,  'MOEX:SEC',     'ФР МБ',            2),
    (3,     1,      1,  'MOEX:CUR',     'ВР МБ',            3),
    (4,     1,      1,  'MOEX:DER',     'СР МБ',            3),
    (11,    NULL,   1,  'SPB',          'СПБ',              5),
    (21,    NULL,   2,  'QUIK:VTB',     'Quik ВТБ',         NULL),
    (22,    NULL,   2,  'QUIK:OPEN',    'Quik Открытие',    NULL)
ON CONFLICT ("Id") DO UPDATE SET
    "Parent_Id" = EXCLUDED."Parent_Id",
    "Type_Id" = EXCLUDED."Type_Id",
    "Code" = EXCLUDED."Code",
    "Name" = EXCLUDED."Name",
    "Place_Id" = EXCLUDED."Place_Id";
