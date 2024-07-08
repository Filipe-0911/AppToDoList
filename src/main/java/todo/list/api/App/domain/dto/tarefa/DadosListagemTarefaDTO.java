package todo.list.api.App.domain.dto.tarefa;

import todo.list.api.App.domain.model.Tarefa;

public record DadosListagemTarefaDTO(
        Long id,
        String titulo,
        String data,
        String descricao,
        boolean concluido) {
    public DadosListagemTarefaDTO(Tarefa tarefa) {
        this(tarefa.getId(),tarefa.getTitulo(), tarefa.getData().toString(), tarefa.getDescricao(), tarefa.isConcluido());
    }
}
