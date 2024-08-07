package todo.list.api.App.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import todo.list.api.App.domain.dto.tarefa.DadosCriacaoTarefasDTO;
import todo.list.api.App.domain.dto.tarefa.DadosDetalhamentoTarefaDTO;
import todo.list.api.App.domain.dto.tarefa.DadosListagemTarefaDTO;
import todo.list.api.App.domain.model.Tarefa;
import todo.list.api.App.domain.model.Usuario;
import todo.list.api.App.domain.repository.TarefaRepository;
import todo.list.api.App.domain.services.validation.tarefa.TarefaValidation;

import java.util.List;

@Service
public class TarefaService {
    @Autowired
    private TarefaRepository tarefaRepository;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private List<TarefaValidation> verificadoresTarefa;

    public DadosListagemTarefaDTO isCompleted(Tarefa tarefa) {
        if (!tarefa.isConcluido()) {
            return new DadosListagemTarefaDTO(tarefa);
        }
        return null;
    }

    public ResponseEntity<Page<DadosListagemTarefaDTO>> listarTarefas(Pageable pageable, HttpServletRequest request) {
        Long id = usuarioService.buscaUsuario(request).getId();
        if (id != null) {
            Page<DadosListagemTarefaDTO> tarefas = tarefaRepository.findAllByUsuarioId(pageable, id)
                    .map(DadosListagemTarefaDTO::new);
            return ResponseEntity.ok(tarefas);
        }
        return null;
    }

    public ResponseEntity<DadosDetalhamentoTarefaDTO> criarTarefa (DadosCriacaoTarefasDTO dadosTarefa, HttpServletRequest request) {
        Long id = usuarioService.buscaUsuario(request).getId();
        Tarefa tarefa = new Tarefa(dadosTarefa);

        verificadoresTarefa.forEach(v -> v.validar(tarefa));
        
        if (id != null) {
            Usuario usuario = usuarioService.buscaUsuario(request);

            tarefa.setUsuario(usuario);
            usuario.setTarefas(tarefa);
            tarefaRepository.save(tarefa);

            DadosDetalhamentoTarefaDTO tarefasDTO =  new DadosDetalhamentoTarefaDTO(tarefa);

            return ResponseEntity.ok(tarefasDTO);
        }
        return ResponseEntity.badRequest().build();
    }

    public ResponseEntity<DadosDetalhamentoTarefaDTO> concluirTarefa(Long id, HttpServletRequest request) {
        Tarefa tarefaParaFinalizar = tarefaRepository.getReferenceById(id);
        Usuario usuario = usuarioService.buscaUsuario(request);

        if (tarefaParaFinalizar.getUsuario().equals(usuario)) {
            tarefaParaFinalizar.setConcluido(true);
            return ResponseEntity.ok(new DadosDetalhamentoTarefaDTO(tarefaParaFinalizar));
        } else return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

    }

    public ResponseEntity<DadosDetalhamentoTarefaDTO> getTarefaEspecifica(HttpServletRequest request, Long idTarefa) {
        Tarefa tarefaEspecifica = tarefaRepository.getReferenceById(idTarefa);
        Usuario usuario = usuarioService.buscaUsuario(request);

        if (tarefaEspecifica.getUsuario().equals(usuario)) {
            return ResponseEntity.ok(new DadosDetalhamentoTarefaDTO(tarefaEspecifica));
        } else return ResponseEntity.notFound().build();
    }

    public ResponseEntity<DadosDetalhamentoTarefaDTO> atualizarInformacoes(Long idTarefa, DadosCriacaoTarefasDTO alteracao, HttpServletRequest request) {
        Usuario usuario = usuarioService.buscaUsuario(request);
        Tarefa tarefaEspecifica = tarefaRepository.getReferenceById(idTarefa);

        if (usuario.getTarefas().contains(tarefaEspecifica)) {
            tarefaEspecifica.atualizarInformacoes(alteracao);
            return ResponseEntity.ok(new DadosDetalhamentoTarefaDTO(tarefaEspecifica));
        }

        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<Void> deletarTarefa(Long idTarefa, HttpServletRequest request) {
        Usuario usuario = usuarioService.buscaUsuario(request);
        Tarefa tarefaEspecifica = tarefaRepository.getReferenceById(idTarefa);

        if (usuario.getTarefas().contains(tarefaEspecifica)) {
            tarefaRepository.delete(tarefaEspecifica);
            usuario.deleteTarefa(tarefaEspecifica);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();

    }

}
