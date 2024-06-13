package todo.list.api.App.domain.dto.materia;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCriacaoMateriaDTO(
    @NotNull
    String nome,
    @NotNull
    @NotBlank
    Long idProva
) {

}
