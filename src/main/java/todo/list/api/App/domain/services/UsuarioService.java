package todo.list.api.App.domain.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import todo.list.api.App.domain.dto.usuario.DadosCriacaoUsuarioDTO;
import todo.list.api.App.domain.dto.usuario.DadosUsuarioDTO;
import todo.list.api.App.domain.model.Assunto;
import todo.list.api.App.domain.model.Materia;
import todo.list.api.App.domain.model.PlanejadorEstudos;
import todo.list.api.App.domain.model.Prova;
import todo.list.api.App.domain.model.Usuario;
import todo.list.api.App.domain.repository.UsuarioRepository;
import todo.list.api.App.domain.services.validation.usuario.UsuarioValidation;
import todo.list.api.App.infra.security.TokenService;

@Service
@Getter
public class UsuarioService {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private List<UsuarioValidation> validadorUsuario;

    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public static String encriptadorSenhaUsuario(String senha) {
        return bCryptPasswordEncoder.encode(senha);
    }

    public ResponseEntity<DadosUsuarioDTO> salvarUsuario (DadosCriacaoUsuarioDTO dadosCriacaoUsuarioDTO) {
        validadorUsuario.forEach(u -> u.validar(dadosCriacaoUsuarioDTO));
        Usuario usuario = new Usuario(dadosCriacaoUsuarioDTO);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok(new DadosUsuarioDTO(usuario));
    }

    public Usuario buscaUsuario(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Long id = tokenService.getClaim(token);
        return usuarioRepository.getReferenceById(id);
    }

    public boolean verificaSeMateriaPertenceAUsuario(Usuario usuario, Materia materia) {
        Prova prova = materia.getProva();

        var provaPertenceAUsuario = usuario.getProvas().contains(prova);
        if (provaPertenceAUsuario) {
            return prova.getListaDeMaterias().contains(materia);
        }
        return false;
    }

    public boolean verificaSeAssuntoPertenceAUsuario(Usuario usuario, Assunto assunto) {
        List<Assunto> listaDeAssuntosUsuario = __pegaListaAssuntoUsuario(usuario);
        return listaDeAssuntosUsuario.contains(assunto);
    }

    public boolean verificaSePlanejadorEstudosPertenceAUsuario(Usuario usuario, PlanejadorEstudos planejadorEstudos) {
        return usuario.getPlanejadorEstudos().contains(planejadorEstudos);
    }
    public boolean verificaSeProvaPertenceAUsuario (HttpServletRequest request, Prova prova) {
        Usuario usuario = buscaUsuario(request);

        return usuario.getProvas().contains(prova);
    }

    private List<Assunto> __pegaListaAssuntoUsuario(Usuario usuario) {
        return usuario.getProvas().stream()
        .flatMap(p -> p.getListaDeMaterias().stream()).toList().stream()
                .flatMap(m -> m.getListaAssuntos().stream())
                .toList();
    }
}
