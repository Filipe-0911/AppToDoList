package todo.list.api.App.domain.dto.questao;

import jakarta.validation.constraints.NotNull;
import todo.list.api.App.domain.dto.alternativa.DadosCriacaoAlternativaDTO;

import java.util.List;

public record DadosCriacaoQuestaoDTO(
        @NotNull
        String textoQuestao,

        @NotNull
        List<DadosCriacaoAlternativaDTO> listaAlternativas,
        Long idAssunto
) {
}
