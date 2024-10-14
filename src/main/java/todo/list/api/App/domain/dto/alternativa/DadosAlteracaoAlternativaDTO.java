package todo.list.api.App.domain.dto.alternativa;

import todo.list.api.App.domain.model.AlternativaQuestao;

public record DadosAlteracaoAlternativaDTO(
        Long id,
        String textoAlternativa,
        boolean ehCorreta
) {
    public DadosAlteracaoAlternativaDTO(AlternativaQuestao a) {
        this(
                a.getId(),
                a.getTextoAlternativa(),
                a.isEhCorreta()
        );
    }
}
