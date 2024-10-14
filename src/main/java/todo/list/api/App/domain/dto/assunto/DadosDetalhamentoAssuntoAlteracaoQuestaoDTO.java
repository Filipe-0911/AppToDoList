package todo.list.api.App.domain.dto.assunto;

import todo.list.api.App.domain.model.Assunto;

public record DadosDetalhamentoAssuntoAlteracaoQuestaoDTO(String nome, Long id) {
    public DadosDetalhamentoAssuntoAlteracaoQuestaoDTO(Assunto a) {
        this(a.getNome(), a.getId());
    }
}
