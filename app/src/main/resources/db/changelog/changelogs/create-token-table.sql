--changeset NoNeedToBother:5

create table user_tokens(
    id bigserial primary key,
    token varchar(64),
    user_id bigserial references users(id),
    invalidated boolean
);