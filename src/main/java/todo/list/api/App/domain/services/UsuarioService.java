package todo.list.api.App.domain.services;

import lombok.Getter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Getter
public class UsuarioService {
    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public static String encriptadorSenhaUsuario(String senha) {
        return bCryptPasswordEncoder.encode(senha);
    }
}
