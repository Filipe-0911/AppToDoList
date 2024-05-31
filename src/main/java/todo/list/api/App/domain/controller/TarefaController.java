package todo.list.api.App.domain.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import todo.list.api.App.domain.dto.tarefa.DadosCriacaoTarefasDTO;
import todo.list.api.App.domain.dto.tarefa.DadosListagemTarefaDTO;
import todo.list.api.App.domain.model.Tarefa;
import todo.list.api.App.domain.repository.TarefaRepository;
import todo.list.api.App.domain.repository.UsuarioRepository;
import todo.list.api.App.domain.model.Usuario;
import todo.list.api.App.infra.security.TokenService;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/tarefas")
public class TarefaController {
    @Autowired
    private TarefaRepository tarefaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private TokenService tokenService;

    @GetMapping
    public List<DadosListagemTarefaDTO> listarTarefas(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Long id = tokenService.getClaim(token);
        if (id != null) {
            Usuario usuario = usuarioRepository.getReferenceById(id);
            return usuario.getTarefas().stream()
                    .map(DadosListagemTarefaDTO::new)
                    .collect(Collectors.toList());
        } else {
            return null;
        }
    }

    @Transactional
    @PostMapping
    public void inserirTarefa(@RequestBody DadosCriacaoTarefasDTO dadosTarefa, HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        Long id = tokenService.getClaim(token);
        if (id != null) {
            Usuario usuario = usuarioRepository.getReferenceById(id);
            Tarefa tarefa = new Tarefa(dadosTarefa);
            tarefa.setUsuario(usuario);
            usuario.setTarefas(tarefa);
        }

    }
}
