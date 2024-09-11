package todo.list.api.App.domain.dto.alternativa;

import todo.list.api.App.domain.model.AlternativaQuestao;

public record DadosDetalhamentoAlternativaDTO(
        Long id,
        String textoAlternativa
) {
    public DadosDetalhamentoAlternativaDTO(AlternativaQuestao alternativa) {
        this(alternativa.getId(), alternativa.getTextoAlternativa());
    }
}
