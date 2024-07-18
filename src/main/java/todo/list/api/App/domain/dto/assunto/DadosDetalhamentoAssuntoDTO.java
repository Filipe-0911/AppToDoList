package todo.list.api.App.domain.dto.assunto;

import java.util.List;

import todo.list.api.App.domain.model.Assunto;
import todo.list.api.App.domain.model.Questao;

public record DadosDetalhamentoAssuntoDTO(
    Long id,
    String nome,
    int quantidadePdf,
    String comentarios,
    List<Long> idQuestoes
) {

    public DadosDetalhamentoAssuntoDTO(Assunto assunto) {
        this(
            assunto.getId(),
            assunto.getNome(),
            assunto.getQuantidadePdf(),
            assunto.getComentarios(),
            assunto.getListaDeQuestoes()
                .stream()
                    .map(Questao::getId)
                    .toList()
        );
    }

}
