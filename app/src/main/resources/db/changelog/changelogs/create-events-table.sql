--changeset NoNeedToBother:2

create table events(
    id bigint primary key not null,
    name varchar not null,
    description varchar not null,
    price float8 not null,
    date timestamp,
    place_id bigint references places(id) on delete cascade
);