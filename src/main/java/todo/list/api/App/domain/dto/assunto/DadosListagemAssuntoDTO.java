package todo.list.api.App.domain.dto.assunto;

import java.util.List;

import todo.list.api.App.domain.dto.questao.DadosListagemQuestoesDTO;
import todo.list.api.App.domain.model.Assunto;

public record DadosListagemAssuntoDTO (
    String nome,
    int quantidadePdf,
    List<DadosListagemQuestoesDTO> listaDeQuestoes
){
    public DadosListagemAssuntoDTO(Assunto assunto) {
        this(
            assunto.getNome(),
            assunto.getQuantidadePdf(),
            assunto.getListaDeQuestoes().stream()
                .map(DadosListagemQuestoesDTO::new)
                .toList()
        );
    }

}
