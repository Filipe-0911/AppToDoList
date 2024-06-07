package todo.list.api.App.domain.dto.tarefa;

import todo.list.api.App.domain.model.Tarefa;

public record DadosDetalhamentoTarefaDTO(
        String titulo,
        String descricao,
        String data,
        boolean concluido) {
    public DadosDetalhamentoTarefaDTO(Tarefa t) {
        this(t.getTitulo(), t.getDescricao(), t.getData().toString(), t.isConcluido());
    }
}
