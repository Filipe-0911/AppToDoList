package todo.list.api.App.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import todo.list.api.App.domain.dto.questao.DadosCriacaoQuestaoDTO;

import java.util.List;

@Table(name = "questoes_para_responder")
@Entity(name = "Questao")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Questao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(columnDefinition = "TEXT")
    private String textoQuestao;

    @Setter
    @ManyToOne
    private Materia materia;

    @Setter
    @ManyToOne
    private Assunto assunto;

    @Setter
    @OneToMany(mappedBy = "questao", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<AlternativaQuestao> listaAlternativaQuestao;

    public Questao(DadosCriacaoQuestaoDTO dados) {
        this.textoQuestao = dados.textoQuestao();
    }

    public void setAlternativa(AlternativaQuestao alternativa) {
        this.listaAlternativaQuestao.add(alternativa);
    }

    public void deletaAlternativa(AlternativaQuestao alternativa) {
        this.listaAlternativaQuestao.remove(alternativa);
    }
}
