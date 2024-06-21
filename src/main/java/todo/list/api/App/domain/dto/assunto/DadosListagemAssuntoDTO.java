package todo.list.api.App.domain.dto.assunto;

import todo.list.api.App.domain.model.Assunto;

public record DadosListagemAssuntoDTO (
    String nome,
    int quantidadePdf,
    int quantidadeQuestoes
){
    public DadosListagemAssuntoDTO(Assunto assunto) {
        this(
            assunto.getNome(),
            assunto.getQuantidadePdf(),
            assunto.getListaDeQuestoes().size()
        );
    }

}
