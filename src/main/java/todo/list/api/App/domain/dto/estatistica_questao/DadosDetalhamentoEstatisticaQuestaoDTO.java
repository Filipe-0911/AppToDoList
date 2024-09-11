package todo.list.api.App.domain.dto.estatistica_questao;

import java.time.LocalDateTime;

import todo.list.api.App.domain.dto.assunto.DadosListagemAssuntoDTO;
import todo.list.api.App.domain.model.EstatisticaQuestao;

public record DadosDetalhamentoEstatisticaQuestaoDTO(
        Long id,
        int questoesFeitas,
        int questoesAcertadas,
        LocalDateTime dataPreenchimento,
        DadosListagemAssuntoDTO dadosAssunto) {

    public DadosDetalhamentoEstatisticaQuestaoDTO(EstatisticaQuestao estatisticaQuestao) {
        this(
                estatisticaQuestao.getId(),
                estatisticaQuestao.getQuestoesFeitas(),
                estatisticaQuestao.getQuestoesAcertadas(),
                estatisticaQuestao.getDataPreenchimento(),
                new DadosListagemAssuntoDTO(estatisticaQuestao.getAssunto())
        );
    }

}
