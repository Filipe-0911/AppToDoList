package todo.list.api.App.domain.services;

import lombok.Getter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import todo.list.api.App.domain.dto.usuario.DadosCriacaoUsuarioDTO;

@Getter
public class UsuarioService {
    private String nome;
    private String login;
    private String senha;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public UsuarioService (DadosCriacaoUsuarioDTO dadosUsuario) {
        this.nome = dadosUsuario.nome();
        this.login = dadosUsuario.login();
        this.senha = __encriptadorSenhaUsuario(dadosUsuario.senha());
    }

    private String __encriptadorSenhaUsuario(String senha) {
        return bCryptPasswordEncoder.encode(senha);
    }
}
