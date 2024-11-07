--changeset NoNeedToBother:7

create table password_change_requests(
    id bigserial primary key,
    new_password varchar(64),
    previous_password varchar(64),
    user_id bigserial references users(id),
    code varchar(4)
);