package todo.list.api.App.domain.dto.questao;

import java.time.LocalDateTime;

import todo.list.api.App.domain.dto.assunto.DadosListagemAssuntoDTO;
import todo.list.api.App.domain.model.Questao;

public record DadosDetalhamentoQuestaoDTO(
        Long id,
        int questoesFeitas,
        int questoesAcertadas,
        LocalDateTime dataPreenchimento,
        DadosListagemAssuntoDTO dadosAssunto) {

    public DadosDetalhamentoQuestaoDTO(Questao questao) {
        this(
                questao.getId(),
                questao.getQuestoesFeitas(),
                questao.getQuestoesAcertadas(),
                questao.getDataPreenchimento(),
                new DadosListagemAssuntoDTO(questao.getAssunto())
        );
    }

}
