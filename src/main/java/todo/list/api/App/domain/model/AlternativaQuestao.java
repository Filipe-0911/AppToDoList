package todo.list.api.App.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import todo.list.api.App.domain.dto.alternativa.DadosCriacaoAlternativaDTO;

@Table(name = "alternativas")
@Entity(name = "AlternativaQuestao")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AlternativaQuestao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String textoAlternativa;
    @ManyToOne
    private Questao questao;

    public AlternativaQuestao(DadosCriacaoAlternativaDTO dadosCriacaoAlternativaDTO) {
        this.textoAlternativa = dadosCriacaoAlternativaDTO.textoAlternativa();
    }
}
