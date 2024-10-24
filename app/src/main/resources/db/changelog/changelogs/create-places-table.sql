--changeset NoNeedToBother:1

create table places(
    id bigint primary key,
    slug varchar(32),
    name varchar(64)
);