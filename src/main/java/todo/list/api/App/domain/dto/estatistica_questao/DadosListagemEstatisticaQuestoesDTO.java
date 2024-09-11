package todo.list.api.App.domain.dto.estatistica_questao;

import java.time.LocalDateTime;

import todo.list.api.App.domain.model.EstatisticaQuestao;

public record DadosListagemEstatisticaQuestoesDTO(
        Long idAssunto,
        String tituloAssunto,
        int questoesFeitas,
        int questoesAcertadas,
        LocalDateTime dataPreenchimento
) {

    public DadosListagemEstatisticaQuestoesDTO(EstatisticaQuestao estatisticaQuestao) {
        this(
                estatisticaQuestao.getAssunto().getId(),
                estatisticaQuestao.getAssunto().getNome(),
                estatisticaQuestao.getQuestoesFeitas(),
                estatisticaQuestao.getQuestoesAcertadas(),
                estatisticaQuestao.getDataPreenchimento());
    }

}
