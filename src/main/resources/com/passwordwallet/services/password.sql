create table if not exists password_wallet.password
(
    id          int auto_increment,
    password    varchar(256) null,
    id_user     int          null,
    web_address varchar(256) null,
    description varchar(256) null,
    login       varchar(30)  null,
    constraint password_id_uindex
        unique (id)
);

