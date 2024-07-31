package todo.list.api.App.domain.dto.tarefa;

import todo.list.api.App.domain.model.Tarefa;

public record DadosDetalhamentoTarefaDTO(
        Long id,
        String titulo,
        String descricao,
        String data,
        boolean concluido) {
    public DadosDetalhamentoTarefaDTO(Tarefa t) {
        this(t.getId(), t.getTitulo(), t.getDescricao(), t.getData().toString(), t.isConcluido());
    }
}
