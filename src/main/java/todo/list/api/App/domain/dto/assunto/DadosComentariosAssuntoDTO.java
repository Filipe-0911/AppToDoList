package todo.list.api.App.domain.dto.assunto;

import todo.list.api.App.domain.model.Assunto;

public record DadosComentariosAssuntoDTO(
        String comentarios
) {
    public DadosComentariosAssuntoDTO(Assunto assunto) {
        this(assunto.getComentarios());
    }
}
