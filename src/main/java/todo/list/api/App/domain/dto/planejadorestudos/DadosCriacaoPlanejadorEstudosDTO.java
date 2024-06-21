package todo.list.api.App.domain.dto.planejadorestudos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonAlias;

import jakarta.validation.constraints.NotNull;

public record DadosCriacaoPlanejadorEstudosDTO(
    @JsonAlias("data_inicio") 
    LocalDateTime dataInicio,

    @JsonAlias("assundo_id") 
    Long assuntoId,

    @NotNull 
    @JsonAlias("data_termino")
    LocalDateTime dataTermino,
    
    @JsonAlias("usuario_id")
    Long usuarioId
) {
}
