package todo.list.api.App.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private int questoesFeitas;
    private int questoesAcertadas;

    @ManyToOne
    private Materia materia;

    public void setQuestoesFeitas(int questoes) {
        this.questoesFeitas += questoes;
    }

    public void setQuestoesAcertadas(int questoes) {
        this.questoesAcertadas += questoes;
    }
}
