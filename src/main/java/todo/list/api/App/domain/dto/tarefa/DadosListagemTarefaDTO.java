package todo.list.api.App.domain.dto.tarefa;

import todo.list.api.App.domain.model.Tarefa;

public record DadosListagemTarefaDTO(
        String titulo,
        String descricao,
        String data) {
    public DadosListagemTarefaDTO(Tarefa tarefa) {
        this(tarefa.getTitulo(), tarefa.getDescricao(), tarefa.getData().toString());
    }
}
