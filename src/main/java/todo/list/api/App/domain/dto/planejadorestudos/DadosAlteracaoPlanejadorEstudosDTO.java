package todo.list.api.App.domain.dto.planejadorestudos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonAlias;

import io.micrometer.common.lang.Nullable;

public record DadosAlteracaoPlanejadorEstudosDTO(
    @Nullable
    @JsonAlias("assunto_id") Long idAssunto,
    @JsonAlias("data_inicio") LocalDateTime dataInicio,
    @JsonAlias("data_termino") LocalDateTime dataTermino
) {

}
