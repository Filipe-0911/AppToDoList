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

    @OneToMany(mappedBy = "assunto", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Questao> listaDeQuestoes = new ArrayList<>();

    @Setter
    @ManyToOne
    private Materia materia;

    public Assunto(DadosCriacaoAsssuntoDTO dadosCriacaoAsssuntoDTO) {
        this.nome = dadosCriacaoAsssuntoDTO.nome();
        this.quantidadePdf = dadosCriacaoAsssuntoDTO.quantidadePdf();
    }

    public void setQuestoes(Questao questao) {
        this.listaDeQuestoes.add(questao);
    }

}
