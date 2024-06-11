package todo.list.api.App.domain.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import todo.list.api.App.domain.dto.tarefa.DadosCriacaoTarefasDTO;
import todo.list.api.App.domain.dto.tarefa.DadosDetalhamentoTarefaDTO;
import todo.list.api.App.domain.dto.tarefa.DadosListagemTarefaDTO;
import todo.list.api.App.domain.repository.TarefaRepository;
import todo.list.api.App.domain.model.Usuario;
import todo.list.api.App.domain.services.TarefaService;
import todo.list.api.App.domain.services.UsuarioService;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {
    @Autowired
    private TarefaRepository tarefaRepository;
    @Autowired
    private TarefaService tarefaService;
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<Page<DadosListagemTarefaDTO>> listarTarefas(@PageableDefault(size=10, page=0, sort = {"data"})Pageable pageable, HttpServletRequest request) {
        Long id = usuarioService.buscaUsuario(request).getId();
        if (id != null) {
            Page<DadosListagemTarefaDTO> tarefas = tarefaRepository.findAllByUsuarioId(pageable, id)
                    .map(DadosListagemTarefaDTO::new);
            return ResponseEntity.ok(tarefas);
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
