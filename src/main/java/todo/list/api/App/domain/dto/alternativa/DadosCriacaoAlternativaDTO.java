package todo.list.api.App.domain.dto.alternativa;

import jakarta.validation.constraints.NotNull;

public record DadosCriacaoAlternativaDTO(
        @NotNull
        String textoAlternativa,
        @NotNull
        boolean ehCorreta
) {
}
