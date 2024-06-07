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

    @GetMapping
    public ResponseEntity<List<DadosListagemTarefaDTO>> listarTarefas(HttpServletRequest request) {
        Long id = __retornaId(request);
        if (id != null) {
            return tarefaService.buscaTarefasUsuario(id);
        }
        return null;
    }
    @GetMapping("/{idTarefa}")
    public ResponseEntity<DadosDetalhamentoTarefaDTO> obterDadosTarefaEspecifica(@PathVariable Long idTarefa, HttpServletRequest request) {
        Long idUsuario = __retornaId(request);
        return tarefaService.getTarefaEspecifica(idUsuario, idTarefa);
    }

    @Transactional
    @PostMapping
    public ResponseEntity<DadosDetalhamentoTarefaDTO> inserirTarefa(@RequestBody DadosCriacaoTarefasDTO dadosTarefa, HttpServletRequest request) {
        Long id = __retornaId(request);
        return tarefaService.criarTarefa(dadosTarefa, id);
    }

    @Transactional
    @PutMapping("/concluir/{idTarefa}")
    public ResponseEntity<DadosDetalhamentoTarefaDTO> concluirTarefa(@PathVariable Long idTarefa, HttpServletRequest request) {
        Long id = __retornaId(request);
        Usuario usuario = usuarioRepository.getReferenceById(id);
        return tarefaService.concluirTarefa(idTarefa, usuario);
    }
    @Transactional
    @PutMapping("/{idTarefa}")
    public ResponseEntity<DadosDetalhamentoTarefaDTO> alterarTarefa(
            @PathVariable Long idTarefa,
            @RequestBody DadosCriacaoTarefasDTO alteracao,
            HttpServletRequest request) {
        Long id = __retornaId(request);
        Usuario usuario = usuarioRepository.getReferenceById(id);

        return tarefaService.atualizarInformacoes(idTarefa, alteracao, usuario);
    }

    private Long __retornaId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return tokenService.getClaim(token);
    }

}
