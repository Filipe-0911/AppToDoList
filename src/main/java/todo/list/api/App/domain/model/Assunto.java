package todo.list.api.App.domain.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import todo.list.api.App.domain.dto.assunto.DadosCriacaoAsssuntoDTO;

@Table(name = "assuntos_materia")
@Entity(name = "Assunto")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Assunto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    private String nome;
    @Setter
    private int quantidadePdf;

    @Setter
    @Column(columnDefinition = "TEXT")
    private String comentarios;

    @OneToMany(mappedBy = "assunto", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<EstatisticaQuestao> listaDeEstatisticaQuestoes = new ArrayList<>();

    @Setter
    @OneToMany(mappedBy = "assunto", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Questao> listaDeQuestoes = new ArrayList<>();

    @Setter
    @ManyToOne
    private Materia materia;

    @OneToMany(mappedBy = "assunto", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<PlanejadorEstudos> listaPlanejadorEstudos = new ArrayList<>();

    public Assunto(DadosCriacaoAsssuntoDTO dadosCriacaoAsssuntoDTO) {
        this.nome = dadosCriacaoAsssuntoDTO.nome();
        this.quantidadePdf = dadosCriacaoAsssuntoDTO.quantidadePdf();
    }

    public void setQuestoes(EstatisticaQuestao estatisticaQuestao) {
        this.listaDeEstatisticaQuestoes.add(estatisticaQuestao);
    }

    public void setPlanejadorEstudos(PlanejadorEstudos planejadorEstudos) {
        this.listaPlanejadorEstudos.add(planejadorEstudos);
    }

    public void deletaPlanejadorEstudos(PlanejadorEstudos planejadorEstudos) {
        this.listaPlanejadorEstudos.remove(planejadorEstudos);
    }

    public void addQuestoes(Questao questao) {
        this.listaDeQuestoes.add(questao);
    }

}
