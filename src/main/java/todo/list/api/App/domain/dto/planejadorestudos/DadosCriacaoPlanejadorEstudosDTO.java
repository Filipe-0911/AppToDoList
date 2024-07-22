package todo.list.api.App.domain.dto.planejadorestudos;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotNull;
import todo.list.api.App.domain.services.DateTimeConverterFromString;

import java.time.LocalDateTime;

public record DadosCriacaoPlanejadorEstudosDTO(
        @NotNull
        @JsonAlias("data_inicio")
        String dataInicio,

        Long assuntoId,

        @NotNull
        @JsonAlias("data_termino")
        String dataTermino
) {
        public LocalDateTime getDataInicio() {
                return DateTimeConverterFromString.parse(dataInicio);
        }

        public LocalDateTime getDataTermino() {
                return DateTimeConverterFromString.parse(dataTermino);
        }
}
