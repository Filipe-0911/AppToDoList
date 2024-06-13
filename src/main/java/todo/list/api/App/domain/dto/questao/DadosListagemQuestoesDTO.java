package todo.list.api.App.domain.dto.questao;

import java.time.LocalDateTime;

import todo.list.api.App.domain.model.Questao;

public record DadosListagemQuestoesDTO(
    int questoesFeitas,
    int questoesAcertadas,
    LocalDateTime dataPreenchimento
) {

    public DadosListagemQuestoesDTO(Questao questao) {
        this(questao.getQuestoesFeitas(), 
        questao.getQuestoesAcertadas(), 
        questao.getDataPreenchimento());
    }

}
