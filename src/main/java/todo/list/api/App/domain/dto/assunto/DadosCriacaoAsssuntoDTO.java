package todo.list.api.App.domain.dto.assunto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCriacaoAsssuntoDTO(
        @NotNull
        @NotBlank
        String nome,
        @NotNull
        int quantidadePdf
) {

}
