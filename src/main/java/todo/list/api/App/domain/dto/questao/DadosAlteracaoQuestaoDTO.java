package todo.list.api.App.domain.dto.questao;

import jakarta.validation.constraints.NotNull;
import todo.list.api.App.domain.dto.alternativa.DadosAlteracaoAlternativaDTO;
import todo.list.api.App.domain.dto.assunto.DadosDetalhamentoAssuntoAlteracaoQuestaoDTO;
import todo.list.api.App.domain.dto.assunto.DadosDetalhamentoAssuntoDTO;
import todo.list.api.App.domain.model.Questao;

import java.util.List;

public record DadosAlteracaoQuestaoDTO(
        @NotNull
        String textoQuestao,

        @NotNull
        List<DadosAlteracaoAlternativaDTO> listaAlternativas,

        Long idAssunto
) {
        public DadosAlteracaoQuestaoDTO(Questao q) {
                this(
                        q.getTextoQuestao(),
                        q.getListaAlternativaQuestao().stream().map(DadosAlteracaoAlternativaDTO::new).toList(),
                        q.getAssunto().getId()
                );
        }
}
