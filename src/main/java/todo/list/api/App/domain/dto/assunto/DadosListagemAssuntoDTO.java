package todo.list.api.App.domain.dto.assunto;

import todo.list.api.App.domain.model.Assunto;

public record DadosListagemAssuntoDTO (
    Long id,
    String nome,
    int quantidadePdf,
    int quantidadeQuestoes
){
    public DadosListagemAssuntoDTO(Assunto assunto) {
        this(
            assunto.getId(),
            assunto.getNome(),
            assunto.getQuantidadePdf(),
            assunto.getListaDeQuestoes().size()
        );
    }

}
