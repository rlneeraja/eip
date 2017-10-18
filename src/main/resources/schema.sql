CREATE TABLE Route(
    rguid             varchar(37)         NULL,
    Version           int                 NULL,
    Condition         varchar(512)        NULL,
    Destionation      varchar(2048)       NULL,
    created_by        varchar(255)        NULL,
    created_datetime  datetime            NULL,
    updated_by        varchar(255)        NULL,
    update_datetime   datetime            NULL,
);