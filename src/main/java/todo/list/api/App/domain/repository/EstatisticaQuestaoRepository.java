package todo.list.api.App.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import todo.list.api.App.domain.dto.mediaquestoes.DadosDetalhamentoMediaQuestoesDTO;
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

    Page<EstatisticaQuestao> findAllByAssuntoId(Pageable pageable, Long id);
}
