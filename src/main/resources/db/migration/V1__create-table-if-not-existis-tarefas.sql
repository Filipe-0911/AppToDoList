create table if not exists tarefas (
        id bigint not null auto_increment,
        data datetime(6),
        descricao varchar(255),
        titulo varchar(255),
        usuario_id bigint,
        primary key (id)
    )