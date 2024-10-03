package todo.list.api.App.domain.dto.questao;

import todo.list.api.App.domain.dto.alternativa.DadosBuscaParaEdicaoAlternativaDTO;
import todo.list.api.App.domain.dto.alternativa.DadosDetalhamentoAlternativaDTO;
import todo.list.api.App.domain.model.Questao;

import java.util.List;

public record DadosBuscaParaEdicaoQuestaoDTO(
        Long id,
        String textoQuestao,
        String nomeMateria,
        List<DadosBuscaParaEdicaoAlternativaDTO> listaAlternativas
) {
    public DadosBuscaParaEdicaoQuestaoDTO(Questao questao) {
        this(
                questao.getId(),
                questao.getTextoQuestao(),
                questao.getMateria().getNome(),
                questao.getListaAlternativaQuestao().stream()
                        .sorted((a, b) -> Math.random() > 0.5 ? 1 : -1)
                        .map(DadosBuscaParaEdicaoAlternativaDTO::new).toList()
        );
    }
}