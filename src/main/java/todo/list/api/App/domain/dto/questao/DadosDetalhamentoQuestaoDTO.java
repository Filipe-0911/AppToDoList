package todo.list.api.App.domain.dto.questao;

import todo.list.api.App.domain.dto.alternativa.DadosDetalhamentoAlternativaDTO;
import todo.list.api.App.domain.model.Questao;

import java.util.List;

public record DadosDetalhamentoQuestaoDTO(
        Long id,
        String textoQuestao,
        String respostaCerta,
        List<DadosDetalhamentoAlternativaDTO> listaAlternativas
) {
    public DadosDetalhamentoQuestaoDTO(Questao questao) {
        this(
                questao.getId(),
                questao.getTextoQuestao(),
                questao.getTextoRespostaCerta(),
                questao.getListaAlternativaQuestao().stream().map(DadosDetalhamentoAlternativaDTO::new).toList()
        );
    }
}
