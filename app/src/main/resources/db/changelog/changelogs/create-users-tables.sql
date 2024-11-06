--changeset NoNeedToBother:3

create table users(
    id bigserial primary key,
    username varchar(32),
    password varchar(64)
);

create table user_roles(
    user_id bigserial references users(id),
    role varchar(24)
);