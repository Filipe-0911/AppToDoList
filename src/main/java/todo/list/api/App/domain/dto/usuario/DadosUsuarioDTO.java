package todo.list.api.App.domain.dto.usuario;

import todo.list.api.App.domain.model.Usuario;

public record DadosUsuarioDTO(
        Long id,
        String login,
        String nome
) {
    public DadosUsuarioDTO(Usuario usuario) {
        this(usuario.getId(), usuario.getLogin(), usuario.getNome());
    }
}
