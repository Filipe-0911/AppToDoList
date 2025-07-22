package todo.list.api.App.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import todo.list.api.App.domain.dto.usuario.DadosCriacaoUsuarioDTO;
import todo.list.api.App.domain.dto.usuario.DadosUsuarioDTO;
import todo.list.api.App.domain.model.Usuario;
import todo.list.api.App.domain.services.UsuarioService;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;
    
    @SecurityRequirement(name =  "bearer-key")
    @GetMapping
    public ResponseEntity<DadosUsuarioDTO> retornaDadosUsuario(HttpServletRequest httpServletRequest) {
        Usuario usuarioBuscado = usuarioService.buscaUsuario(httpServletRequest);
        return ResponseEntity.ok(new DadosUsuarioDTO(usuarioBuscado, httpServletRequest.getHeader("Authorization")));
    }
    @Transactional
    @PostMapping("/addUser")
    public ResponseEntity<DadosUsuarioDTO> criarUsuario(@RequestBody @Valid DadosCriacaoUsuarioDTO dados) {
        return usuarioService.salvarUsuario(dados);
    }
}
