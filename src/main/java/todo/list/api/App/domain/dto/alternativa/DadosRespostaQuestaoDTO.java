package todo.list.api.App.domain.dto.alternativa;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosRespostaQuestaoDTO(
        @NotNull
        @NotBlank
        String respostaEscolhida
) {
}
