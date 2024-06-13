package todo.list.api.App.domain.dto.questao;

import java.time.LocalDateTime;

public record DadosCriacaoQuestaoDTO(
    LocalDateTime dataPreenchimento,
    int questoesAcertadas,
    int questoesFeitas
) {

}
