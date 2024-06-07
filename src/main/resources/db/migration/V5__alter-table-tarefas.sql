alter table tarefas
    add column(
    concluido tinyint(1));

update tarefas set concluido = 0;

