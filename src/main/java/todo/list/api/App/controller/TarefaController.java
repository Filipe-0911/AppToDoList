package todo.list.api.App.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import todo.list.api.App.domain.dto.tarefa.DadosCriacaoTarefasDTO;
import todo.list.api.App.domain.dto.tarefa.DadosDetalhamentoTarefaDTO;
import todo.list.api.App.domain.dto.tarefa.DadosListagemTarefaDTO;
import todo.list.api.App.domain.services.TarefaService;

@RestController
@RequestMapping("/tarefas")
@SecurityRequirement(name =  "bearer-key")
public class TarefaController {
    @Autowired
    private TarefaService tarefaService;

    @GetMapping
    public ResponseEntity<Page<DadosListagemTarefaDTO>> listarTarefas(@PageableDefault(size=10, page=0, sort = {"data"})Pageable pageable, HttpServletRequest request) {
        return tarefaService.listarTarefas(pageable, request);
    }
    
    @GetMapping("/{idTarefa}")
    public ResponseEntity<DadosDetalhamentoTarefaDTO> obterDadosTarefaEspecifica(@PathVariable Long idTarefa, HttpServletRequest request) {
        return tarefaService.getTarefaEspecifica(request, idTarefa);
    }

    @Transactional
    @PostMapping
    public ResponseEntity<DadosDetalhamentoTarefaDTO> inserirTarefa(@RequestBody DadosCriacaoTarefasDTO dadosTarefa, HttpServletRequest request) {
        return tarefaService.criarTarefa(dadosTarefa, request);
    }

    @Transactional
    @PutMapping("/concluir/{idTarefa}")
    public ResponseEntity<DadosDetalhamentoTarefaDTO> concluirTarefa(@PathVariable Long idTarefa, HttpServletRequest request) {
        return tarefaService.concluirTarefa(idTarefa, request);
    }

    @Transactional
    @PutMapping("/{idTarefa}")
    public ResponseEntity<DadosDetalhamentoTarefaDTO> alterarTarefa(@PathVariable Long idTarefa, @RequestBody DadosCriacaoTarefasDTO alteracao, HttpServletRequest request) {
        return tarefaService.atualizarInformacoes(idTarefa, alteracao, request);
    }

}
