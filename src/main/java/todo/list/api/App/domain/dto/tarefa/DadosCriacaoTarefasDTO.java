package todo.list.api.App.domain.dto.tarefa;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCriacaoTarefasDTO(
        @NotNull
        @NotBlank
        String titulo,

        String descricao,

        @NotNull
        @NotBlank
        String data) {
}
