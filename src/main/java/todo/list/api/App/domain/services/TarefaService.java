package todo.list.api.App.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import todo.list.api.App.domain.dto.tarefa.DadosCriacaoTarefasDTO;
import todo.list.api.App.domain.dto.tarefa.DadosDetalhamentoTarefaDTO;
import todo.list.api.App.domain.dto.tarefa.DadosListagemTarefaDTO;
import todo.list.api.App.domain.model.Tarefa;
import todo.list.api.App.domain.model.Usuario;
import todo.list.api.App.domain.repository.TarefaRepository;
import todo.list.api.App.domain.repository.UsuarioRepository;
import todo.list.api.App.infra.exception.TratadorDeErros;

import java.util.List;

@Service
public class TarefaService {
    @Autowired
    private TarefaRepository tarefaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private TratadorDeErros tratadorDeErros;

    public DadosListagemTarefaDTO isCompleted(Tarefa tarefa) {
        if (!tarefa.isConcluido()) {
            return new DadosListagemTarefaDTO(tarefa);
        }
        return null;
    }

    public ResponseEntity<List<DadosListagemTarefaDTO>> buscaTarefasUsuario(Long id) {
        Usuario usuario = usuarioRepository.getReferenceById(id);
        List<DadosListagemTarefaDTO> listaTarefas = usuario.getTarefas().stream()
                .filter(t -> !t.isConcluido())
                .map(DadosListagemTarefaDTO::new)
                .toList();

        return ResponseEntity.ok(listaTarefas);

    }

    public ResponseEntity<DadosDetalhamentoTarefaDTO> criarTarefa (DadosCriacaoTarefasDTO dadosTarefa, Long id) {
        if (id != null) {
            Usuario usuario = usuarioRepository.getReferenceById(id);
            Tarefa tarefa = new Tarefa(dadosTarefa);
            tarefa.setUsuario(usuario);
            usuario.setTarefas(tarefa);

            DadosDetalhamentoTarefaDTO tarefasDTO =  new DadosDetalhamentoTarefaDTO(tarefa.getTitulo(), tarefa.getDescricao(), tarefa.getData().toString(), false);

            return ResponseEntity.ok(tarefasDTO);
        }

        return ResponseEntity.badRequest().build();
    }

    public ResponseEntity<DadosDetalhamentoTarefaDTO> concluirTarefa(Long id, Usuario usuario) {
        Tarefa tarefaParaFinalizar = tarefaRepository.getReferenceById(id);

        if (tarefaParaFinalizar.getUsuario().equals(usuario)) {
            tarefaParaFinalizar.setConcluido(true);
            return ResponseEntity.ok(new DadosDetalhamentoTarefaDTO(tarefaParaFinalizar));
        } else return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

    }

    public ResponseEntity<DadosDetalhamentoTarefaDTO> getTarefaEspecifica(Long idUsuario, Long idTarefa) {
        Tarefa tarefaEspecifica = tarefaRepository.getReferenceById(idTarefa);
        Usuario usuario = usuarioRepository.getReferenceById(idUsuario);
        if (tarefaEspecifica.getUsuario().equals(usuario)) {
            return ResponseEntity.ok(new DadosDetalhamentoTarefaDTO(tarefaEspecifica));
        } else return ResponseEntity.notFound().build();
    }

    public ResponseEntity<DadosDetalhamentoTarefaDTO> atualizarInformacoes(Long idTarefa, DadosCriacaoTarefasDTO alteracao, Usuario usuario) {
        Tarefa tarefaEspecifica = tarefaRepository.getReferenceById(idTarefa);
        tarefaEspecifica.atualizarInformacoes(alteracao);
        return ResponseEntity.ok(new DadosDetalhamentoTarefaDTO(tarefaEspecifica));
    }
}
