package todo.list.api.App.domain.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import todo.list.api.App.domain.dto.estatistica_questao.DadosCriacaoEstatisticaQuestaoDTO;

@Table(name = "estatisticas_questoes")
@Entity(name = "EstatisticaQuestao")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class EstatisticaQuestao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataPreenchimento;

    private int questoesFeitas;
    private int questoesAcertadas;

    @Setter
    @ManyToOne
    private Assunto assunto;

    public void setQuestoesFeitas(int questoes) {
        this.questoesFeitas += questoes;
    }

    public void setQuestoesAcertadas(int questoes) {
        this.questoesAcertadas += questoes;
    }

    public EstatisticaQuestao(@Valid DadosCriacaoEstatisticaQuestaoDTO dadosQuestao) {
        this.dataPreenchimento = dadosQuestao.dataPreenchimento();
        this.questoesAcertadas = dadosQuestao.questoesAcertadas();
        this.questoesFeitas = dadosQuestao.questoesFeitas();
    }
}
