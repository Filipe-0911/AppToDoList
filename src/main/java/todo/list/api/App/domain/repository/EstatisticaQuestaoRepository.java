package todo.list.api.App.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import todo.list.api.App.domain.dto.estatistica_questao.DadosListagemEstatisticaQuestoesDTO;
import todo.list.api.App.domain.dto.mediaquestoes.DadosDetalhamentoMediaQuestoesDTO;
import todo.list.api.App.domain.dto.mediaquestoes.MediaQuestoesPorAssuntoDTO;
import todo.list.api.App.domain.dto.mediaquestoes.MediaQuestoesPorMateriaDTO;

import todo.list.api.App.domain.model.EstatisticaQuestao;

public interface EstatisticaQuestaoRepository extends JpaRepository<EstatisticaQuestao, Long> {

    EstatisticaQuestao findByDataPreenchimentoAndAssuntoId(LocalDateTime dataPreenchimento, Long assuntoId);
    
    @Query("""
        SELECT 
            new todo.list.api.App.domain.dto.mediaquestoes.DadosDetalhamentoMediaQuestoesDTO(
            date(q.dataPreenchimento) as dataPreenchimento,
            assunto.nome as nome, 
            sum(q.questoesFeitas) as questoesFeitas, 
            sum(q.questoesAcertadas) as questoesCorretas, 
            (sum(q.questoesAcertadas) * 100.0) / sum(q.questoesFeitas) as porcentagem
        )
        FROM EstatisticaQuestao q 
        INNER JOIN q.assunto assunto 
        WHERE q.assunto.id IN :idList
        GROUP BY assunto.nome, date(q.dataPreenchimento)
        ORDER BY date(q.dataPreenchimento) 
        """)
    Page<DadosDetalhamentoMediaQuestoesDTO> calcularEstatisticasPorDia(Pageable pageable, List<Long> idList);

    @Query("""
            SELECT
                new todo.list.api.App.domain.dto.mediaquestoes.MediaQuestoesPorMateriaDTO(
                    SUM(n.questoesCorretas) AS questoesCorretas,
                    SUM(n.questoesRespondidas) AS questoesRespondidas,
                    SUM(n.questoesCorretas) * 100.0 / NULLIF(SUM(n.questoesRespondidas), 0) AS porcentagemAcertoMateria,
                    n.idMateria,
                    m.nome
                )
            FROM
                (SELECT
                    SUM(e.questoesAcertadas) AS questoesCorretas,
                    SUM(e.questoesFeitas) AS questoesRespondidas,
                    a.materia.id AS idMateria
                FROM
                    EstatisticaQuestao e
                INNER JOIN
                    Assunto a ON e.assunto.id = a.id
                GROUP BY
                    e.assunto.id, a.materia.id) AS n
            JOIN Materia m ON n.idMateria = m.id
            WHERE m.id = :idMateria
            GROUP BY
                n.idMateria, m.nome
        """)
    Optional<MediaQuestoesPorMateriaDTO> buscarMediaQuestoesPorMateria(Long idMateria);

    @Query("""
            SELECT
            new todo.list.api.App.domain.dto.mediaquestoes.MediaQuestoesPorAssuntoDTO(
              e.assunto.id AS idAssunto,
              a.nome,
              SUM(e.questoesFeitas) AS questoesFeitas,
              SUM(e.questoesAcertadas) AS questoesCorretas,
              SUM(e.questoesAcertadas) * 100.0 / NULLIF(SUM(e.questoesFeitas), 0) AS porcentagem
            )
            FROM EstatisticaQuestao e
            INNER JOIN Assunto a ON a.id = e.assunto.id
            WHERE e.assunto.id = :idAssunto
            GROUP BY e.assunto.id, a.nome
            """)
    Optional<MediaQuestoesPorAssuntoDTO> buscaMediaQuestoesPorAssunto(Long idAssunto);

    Page<EstatisticaQuestao> findAllByAssuntoId(Pageable pageable, Long id);
}
