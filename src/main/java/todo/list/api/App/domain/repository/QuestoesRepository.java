package todo.list.api.App.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import todo.list.api.App.domain.model.Questao;

import java.util.List;

public interface QuestoesRepository extends JpaRepository<Questao, Long> {
    List<Questao> findAllByMateriaId(Long idMateria);
}
