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
import todo.list.api.App.domain.dto.questao.DadosCriacaoQuestaoDTO;

@Table(name = "questoes")
@Entity(name = "Questao")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Questao {
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

    public Questao(@Valid DadosCriacaoQuestaoDTO dadosQuestao) {
        this.dataPreenchimento = dadosQuestao.dataPreenchimento();
        this.questoesAcertadas = dadosQuestao.questoesAcertadas();
        this.questoesFeitas = dadosQuestao.questoesFeitas();
    }
}
