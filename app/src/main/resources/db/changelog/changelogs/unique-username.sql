--changeset NoNeedToBother:4

alter table users add constraint users_unique_username unique(username);