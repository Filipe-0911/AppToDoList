package todo.list.api.App.domain.dto.assunto;

import java.util.List;

import todo.list.api.App.domain.dto.questao.DadosListagemQuestoesDTO;
import todo.list.api.App.domain.model.Assunto;

public record DadosDetalhamentoAssuntoDTO(
    Long id,
    String nome,
    int quantidadePdf,
    List<DadosListagemQuestoesDTO> listaQuestoes
) {

    public DadosDetalhamentoAssuntoDTO(Assunto assunto) {
        this(
            assunto.getId(),
            assunto.getNome(),
            assunto.getQuantidadePdf(),
            assunto.getListaDeQuestoes()
                .stream()
                    .map(DadosListagemQuestoesDTO::new)
                    .toList()
        );
    }

}
