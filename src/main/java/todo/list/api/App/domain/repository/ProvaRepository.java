package todo.list.api.App.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import todo.list.api.App.domain.model.Prova;

public interface ProvaRepository extends JpaRepository<Prova, Long>{

    Page<Prova> findAllByUsuarioId(Pageable pageable, Long id);

    Prova findByTitulo(String titulo);
}
