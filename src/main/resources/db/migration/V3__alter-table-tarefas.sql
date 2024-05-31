    alter table tarefas
       add constraint FK2vwr1gcbcpkerkhk9ktxxbep
       foreign key (usuario_id)
       references usuarios (id)