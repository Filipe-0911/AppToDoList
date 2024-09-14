package todo.list.api.App.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
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
    @Setter
    private String nome;

    @Setter
    @ManyToOne
    private Prova prova;

    @OneToMany(mappedBy = "materia", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Assunto> listaAssuntos = new ArrayList<>();

    @OneToMany(mappedBy = "materia", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Questao> listaQuestoes = new ArrayList<>();

    public Materia(DadosCriacaoMateriaDTO dadosMateria) {
        this.nome = dadosMateria.nome();
    }

    public void setAssunto(Assunto assunto) {
        this.listaAssuntos.add(assunto);
    }

    public void deleteAssunto(Assunto assunto) {
        this.listaAssuntos = listaAssuntos.stream()
            .filter(a -> !Objects.equals(a.getId(), assunto.getId()))
            .toList();
    }

    public void setAssuntos(List<Assunto> assuntos) {
        assuntos.forEach(this::setAssunto);
    }
    public void setQuestao(Questao questao) {
        this.listaQuestoes.add(questao);
    }
    public void removeQuestao(Questao questao) {
        this.listaQuestoes = this.listaQuestoes.stream().filter(q -> !q.getId().equals(questao.getId())).toList();
    }
    
    
}
