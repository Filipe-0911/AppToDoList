package todo.list.api.App.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import todo.list.api.App.domain.model.PlanejadorEstudos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PlanejadorEstudosRepository extends JpaRepository<PlanejadorEstudos, Object>{
    Page<PlanejadorEstudos> findAllByAssuntoIdAndCanceladoIsFalse(Pageable pageable, Long assuntoId);
    boolean existsByDataInicioAndUsuarioIdAndCanceladoIsFalse(LocalDateTime localDateTime, Long usuarioId);

    @Query("""
    SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END 
    FROM PlanejadorEstudos p 
    WHERE FUNCTION('DATE', p.dataInicio) = :dataInformada 
    AND p.usuario.id = :usuarioId 
    AND p.cancelado = false 
    AND p.id != :idPlanejador
    """)
    boolean verificaSeExisteUmPlanejadorDiferenteParaOHorarioProposto(LocalDateTime dataInformada, Long usuarioId, Long idPlanejador);

    @Query("SELECT p FROM PlanejadorEstudos p WHERE FUNCTION('DATE', p.dataInicio) = :dataInicioInformada AND p.usuario.id = :id AND p.cancelado = false")
    List<PlanejadorEstudos> buscaTodosPorDataEUsuarioId(LocalDate dataInicioInformada, Long id);
}
