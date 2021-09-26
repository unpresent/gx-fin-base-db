-- Table: Base.Providers_Types

-- DROP TABLE "Base"."Providers_Types";

CREATE TABLE IF NOT EXISTS "Base"."Providers_Types"
(
    "Id"            smallint                                            NOT NULL,
    "RootType_Id"   smallint                                                NULL,
    "Parent_Id"     smallint                                                NULL,
    "Code"          varchar(50)     COLLATE pg_catalog."default"        NOT NULL,
    "Name"          varchar(500)    COLLATE pg_catalog."default"            NULL,
    CONSTRAINT "Providers_Types_pkey" PRIMARY KEY ("Id"),
    CONSTRAINT "Providers_Types_fkey_Parent" FOREIGN KEY ("Parent_Id") REFERENCES "Base"."Providers_Types" ("Id"),
    CONSTRAINT "Providers_Types_fkey_Root" FOREIGN KEY ("RootType_Id") REFERENCES "Base"."Providers_Types" ("Id")
) TABLESPACE pg_default;

ALTER TABLE "Base"."Providers_Types" OWNER to gxfin;

INSERT INTO "Base"."Providers_Types" ("Id", "RootType_Id", "Parent_Id", "Code", "Name")
VALUES
    (1, NULL, NULL, 'EX', 'Биржа'),
    (2, NULL, NULL, 'TS', 'Торговая система')
ON CONFLICT ("Id") DO UPDATE SET
    "RootType_Id" = EXCLUDED."RootType_Id",
    "Parent_Id" = EXCLUDED."Parent_Id",
    "Code" = EXCLUDED."Code",
    "Name" = EXCLUDED."Name";
