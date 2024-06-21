package todo.list.api.App.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import todo.list.api.App.domain.model.PlanejadorEstudos;

public interface PlanejadorEstudosRepository extends JpaRepository<PlanejadorEstudos, Object>{
    Page<PlanejadorEstudos> findAllByAssuntoId(Pageable pageable, Long assuntoId);
}
