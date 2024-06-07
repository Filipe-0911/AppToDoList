package todo.list.api.App.domain.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import todo.list.api.App.domain.model.Usuario;
import todo.list.api.App.domain.repository.UsuarioRepository;
import todo.list.api.App.infra.security.TokenService;

@Service
@Getter
public class UsuarioService {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public static String encriptadorSenhaUsuario(String senha) {
        return bCryptPasswordEncoder.encode(senha);
    }

    public Usuario buscaUsuario(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Long id = tokenService.getClaim(token);
        return usuarioRepository.getReferenceById(id);
    }
}
