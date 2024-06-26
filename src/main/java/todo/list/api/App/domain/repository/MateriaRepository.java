package todo.list.api.App.domain.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import todo.list.api.App.domain.model.Materia;

public interface MateriaRepository extends JpaRepository<Materia, Long>{
    Page<Materia> findByProvaId(Pageable pageable, Long idProva);

    List<Materia> findAllByProvaId(Long idProva);

    Materia findByNome(String nome);

}