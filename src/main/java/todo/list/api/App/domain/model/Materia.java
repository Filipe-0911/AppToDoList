package todo.list.api.App.domain.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import todo.list.api.App.domain.dto.materia.DadosCriacaoMateriaDTO;

@Table(name = "materias")
@Entity(name = "Materia")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Materia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @ManyToOne
    private Prova prova;

    @OneToMany(mappedBy = "materia", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Questao> listaQuestoes = new ArrayList<>();

    public Materia(DadosCriacaoMateriaDTO dadosMateria) {
        this.nome = dadosMateria.nome();
    }

    public void setQuestoes(Questao questao) {
        this.listaQuestoes.add(questao);
    }

    public void setProva(Prova prova) {
        this.prova = prova;
    }
    
    
}
