package todo.list.api.App.domain.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import todo.list.api.App.domain.dto.tarefa.DadosCriacaoTarefasDTO;
import todo.list.api.App.domain.dto.tarefa.DadosDetalhamentoTarefaDTO;
import todo.list.api.App.domain.dto.tarefa.DadosListagemTarefaDTO;
import todo.list.api.App.domain.repository.TarefaRepository;
import todo.list.api.App.domain.repository.UsuarioRepository;
import todo.list.api.App.domain.model.Usuario;
import todo.list.api.App.domain.services.TarefaService;
import todo.list.api.App.domain.services.UsuarioService;
import todo.list.api.App.infra.security.TokenService;

import java.util.List;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {
    @Autowired
    private TarefaRepository tarefaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private TarefaService tarefaService;
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<DadosListagemTarefaDTO>> listarTarefas(HttpServletRequest request) {
        Long id = usuarioService.buscaUsuario(request).getId();
        if (id != null) {
            return tarefaService.buscaTarefasUsuario(id);
        }
        return null;
    }
    @GetMapping("/{idTarefa}")
    public ResponseEntity<DadosDetalhamentoTarefaDTO> obterDadosTarefaEspecifica(@PathVariable Long idTarefa, HttpServletRequest request) {
        Long idUsuario = usuarioService.buscaUsuario(request).getId();
        return tarefaService.getTarefaEspecifica(idUsuario, idTarefa);
    }
    @Transactional
    @PostMapping
    public ResponseEntity<DadosDetalhamentoTarefaDTO> inserirTarefa(@RequestBody DadosCriacaoTarefasDTO dadosTarefa, HttpServletRequest request) {
        Long id = usuarioService.buscaUsuario(request).getId();
        return tarefaService.criarTarefa(dadosTarefa, id);
    }
    @Transactional
    @PutMapping("/concluir/{idTarefa}")
    public ResponseEntity<DadosDetalhamentoTarefaDTO> concluirTarefa(@PathVariable Long idTarefa, HttpServletRequest request) {
        Usuario usuario = usuarioService.buscaUsuario(request);
        return tarefaService.concluirTarefa(idTarefa, usuario);
    }
    @Transactional
    @PutMapping("/{idTarefa}")
    public ResponseEntity<DadosDetalhamentoTarefaDTO> alterarTarefa(
            @PathVariable Long idTarefa,
            @RequestBody DadosCriacaoTarefasDTO alteracao,
            HttpServletRequest request) {

        Usuario usuario = usuarioService.buscaUsuario(request);
        return tarefaService.atualizarInformacoes(idTarefa, alteracao, usuario);
    }

}
