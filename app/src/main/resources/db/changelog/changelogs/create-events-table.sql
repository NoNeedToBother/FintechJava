--changeset NoNeedToBother:2

create table events(
    id bigserial primary key,
    name varchar,
    description varchar,
    price float8,
    date timestamp,
    place_id bigserial references places(id) on delete cascade
);