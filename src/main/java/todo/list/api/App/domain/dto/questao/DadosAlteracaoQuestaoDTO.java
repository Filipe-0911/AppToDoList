package todo.list.api.App.domain.dto.questao;

import jakarta.validation.constraints.NotNull;
import todo.list.api.App.domain.dto.alternativa.DadosCriacaoAlternativaDTO;

import java.util.List;

public record DadosAlteracaoQuestaoDTO(
        @NotNull
        String textoQuestao,

        @NotNull
        String respostaCerta,

        @NotNull
        List<DadosCriacaoAlternativaDTO> listaAlternativas
) {
}
