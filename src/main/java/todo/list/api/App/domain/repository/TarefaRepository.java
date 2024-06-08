package todo.list.api.App.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import todo.list.api.App.domain.dto.tarefa.DadosListagemTarefaDTO;
import todo.list.api.App.domain.model.Tarefa;

import java.util.Optional;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    Page<Tarefa> findAllByUsuarioId(Pageable pageable, Long id);
}
