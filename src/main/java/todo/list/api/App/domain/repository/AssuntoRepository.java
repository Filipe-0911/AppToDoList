package todo.list.api.App.domain.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import todo.list.api.App.domain.model.Assunto;

public interface AssuntoRepository extends JpaRepository<Assunto, Long>{
    Page<Assunto> findByMateriaId(Pageable pageable, Long idMateria);
}
