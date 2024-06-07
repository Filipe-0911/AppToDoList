package todo.list.api.App.domain.dto.tarefa;

import todo.list.api.App.domain.model.Tarefa;

public record DadosListagemTarefaDTO(
        Long id,
        String titulo,
        String data) {
    public DadosListagemTarefaDTO(Tarefa tarefa) {
        this(tarefa.getId(),tarefa.getTitulo(), tarefa.getData().toString());
    }
}
