package todo.list.api.App.domain.dto.usuario;

import jakarta.annotation.Nullable;
import todo.list.api.App.domain.dto.DadosTokenJWT;
import todo.list.api.App.domain.model.Usuario;

public record DadosUsuarioDTO(
        Long id,
        String login,
        String nome,
        @Nullable
        DadosTokenJWT token
) {
    public DadosUsuarioDTO(Usuario usuario) {

        this(usuario.getId(), usuario.getLogin(), usuario.getNome(), null);
    }

    public DadosUsuarioDTO(Usuario usuario, String token) {

        this(usuario.getId(), usuario.getLogin(), usuario.getNome(), new DadosTokenJWT(token));
    }
}
