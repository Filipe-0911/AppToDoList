create table if not exists usuarios (
        id bigint not null auto_increment,
        login varchar(255),
        senha varchar(255),
        primary key (id)
    )