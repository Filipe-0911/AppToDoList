package todo.list.api.App.domain.dto.alternativa;

import todo.list.api.App.domain.model.AlternativaQuestao;

public record DadosBuscaParaEdicaoAlternativaDTO(Long id, String textoAlternativa, boolean ehCorreta) {
    public DadosBuscaParaEdicaoAlternativaDTO(AlternativaQuestao alternativa) {
        this(
                alternativa.getId(),
                alternativa.getTextoAlternativa(),
                alternativa.isEhCorreta()

        );
    }
}
