package todo.list.api.App.domain.dto.questao;

import java.time.LocalDateTime;

import todo.list.api.App.domain.model.Questao;

public record DadosListagemQuestoesDTO(
        Long idAssunto,
        String tituloAssunto,
        int questoesFeitas,
        int questoesAcertadas,
        LocalDateTime dataPreenchimento
) {

    public DadosListagemQuestoesDTO(Questao questao) {
        this(
                questao.getAssunto().getId(),
                questao.getAssunto().getNome(),
                questao.getQuestoesFeitas(),
                questao.getQuestoesAcertadas(),
                questao.getDataPreenchimento());
    }

}
