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
    private String textoQuestao;
    @Setter
    private String textoRespostaCerta;

    @Setter
    @ManyToOne
    private Materia materia;

    @Setter
    @OneToMany(mappedBy = "questao", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<AlternativaQuestao> listaAlternativaQuestao;

    public Questao(DadosCriacaoQuestaoDTO dados) {
        this.textoQuestao = dados.textoQuestao();
        this.textoRespostaCerta = dados.respostaCerta();
    }

    public void setAlternativa(AlternativaQuestao alternativa) {
        this.listaAlternativaQuestao.add(alternativa);
    }

}
