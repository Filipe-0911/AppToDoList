package todo.list.api.App.domain.dto.materia;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotNull;

public record DadosAlteracaoMateriaDTO(
        @NotNull
        String nome
) {
}
