package todo.list.api.App.domain.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import todo.list.api.App.domain.dto.usuario.DadosCriacaoUsuarioDTO;
import todo.list.api.App.domain.dto.usuario.DadosUsuarioDTO;
import todo.list.api.App.domain.model.Usuario;
import todo.list.api.App.domain.repository.UsuarioRepository;
import todo.list.api.App.domain.services.GetIdFromTokenString;

import todo.list.api.App.domain.services.UsuarioService;
import todo.list.api.App.infra.security.TokenService;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private GetIdFromTokenString getIdFromTokenString;
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<DadosUsuarioDTO> retornaDadosUsuario(HttpServletRequest httpServletRequest) {
        Usuario usuarioBuscado = usuarioService.buscaUsuario(httpServletRequest);
        return ResponseEntity.ok(new DadosUsuarioDTO(usuarioBuscado));
    }

    @Transactional
    @PostMapping("/addUser")
    public ResponseEntity<DadosUsuarioDTO> criarUsuario(@RequestBody @Valid DadosCriacaoUsuarioDTO dados) {
        Usuario usuario = new Usuario(dados);
        repository.save(usuario);

        return ResponseEntity.ok(new DadosUsuarioDTO(usuario));

    }
}
