package todo.list.api.App.domain.dto.estatistica_questao;

import java.time.LocalDateTime;

public record DadosCriacaoEstatisticaQuestaoDTO(
    LocalDateTime dataPreenchimento,
    int questoesAcertadas,
    int questoesFeitas
) {


}
