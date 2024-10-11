package todo.list.api.App.domain.dto.questao;

import todo.list.api.App.domain.dto.alternativa.DadosDetalhamentoAlternativaDTO;
import todo.list.api.App.domain.model.Questao;

import java.util.List;

public record DadosDetalhamentoQuestaoDTO(
        String nomeAssunto,
        Long id,
        String textoQuestao,
        String nomeMateria,
        List<DadosDetalhamentoAlternativaDTO> listaAlternativas
) {
    public DadosDetalhamentoQuestaoDTO(Questao questao) {
        this(
                questao.getAssunto() != null ? questao.getAssunto().getNome() : null,
                questao.getId(),
                questao.getTextoQuestao(),
                questao.getMateria().getNome(),
                questao.getListaAlternativaQuestao().stream()
                        .sorted((a, b) -> Math.random() > 0.5 ? 1 : -1)
                        .map(DadosDetalhamentoAlternativaDTO::new).toList()
        );
    }
}
