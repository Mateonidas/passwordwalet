create table if not exists password_wallet.user
(
    id             int auto_increment,
    login          varchar(30)  null,
    password_hash  varchar(512) null,
    salt           varchar(20)  null,
    used_algorithm varchar(10)  null,
    constraint user_id_uindex
        unique (id),
    constraint user_login_uindex
        unique (login)
);