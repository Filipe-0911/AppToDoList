package todo.list.api.App.domain.dto.planejadorestudos;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotNull;
import todo.list.api.App.domain.services.DataService;

import java.time.LocalDateTime;

public record DadosCriacaoPlanejadorEstudosDTO(
        @NotNull
        @JsonAlias("data_inicio")
        String dataInicio,

        Long assuntoId,

        @NotNull
        @JsonAlias("data_termino")
        String dataTermino,

        boolean revisao
) {
        public LocalDateTime getDataInicio() {
                return DataService.parse(dataInicio);
        }

        public LocalDateTime getDataTermino() {
                return DataService.parse(dataTermino);
        }
}
