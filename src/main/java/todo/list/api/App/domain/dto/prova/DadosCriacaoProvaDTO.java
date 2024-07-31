package todo.list.api.App.domain.dto.prova;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotNull;

public record DadosCriacaoProvaDTO(
    @NotNull
    String titulo,
    @NotNull
    LocalDateTime dataDaProva,
    @NotNull
    String corDaProva
) {

}
