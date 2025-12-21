package todo.list.api.App.domain.dto.questao;

import todo.list.api.App.domain.dto.alternativa.DadosAlteracaoAlternativaDTO;
import todo.list.api.App.domain.dto.assunto.DadosDetalhamentoAssuntoAlteracaoQuestaoDTO;
import todo.list.api.App.domain.model.Questao;

import java.util.List;

public record DadosDetalhamentoAlteracaoQuestaoDTO(
        Long id,
        String textoQuestao,
        String nomeMateria,
        List<DadosAlteracaoAlternativaDTO> listaAlternativas,
        DadosDetalhamentoAssuntoAlteracaoQuestaoDTO assunto
) {
    public DadosDetalhamentoAlteracaoQuestaoDTO(Questao questao) {
        this(
                questao.getId(),
                questao.getTextoQuestao(),
                questao.getMateria().getNome(),
                questao.getListaAlternativaQuestao().stream()
                        .sorted((a, b) -> Math.random() > 0.5 ? 1 : -1)
                        .map(DadosAlteracaoAlternativaDTO::new).toList(),
                questao.getAssunto() != null ? new DadosDetalhamentoAssuntoAlteracaoQuestaoDTO(questao.getAssunto()) : null
        );
    }
}
