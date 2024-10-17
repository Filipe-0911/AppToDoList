package todo.list.api.App.domain.dto.mediaquestoes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MediaQuestoesPorMateriaDTO {
    private Long questoesCorretas;
    private Long questoesRespondidas;
    private Double porcentagemAcertoMateria;
    private Long idMateria;
    private String nome;

    public MediaQuestoesPorMateriaDTO(Long questoesCorretas, Long questoesRespondidas, Double porcentagemAcertoMateria, Long idMateria, String nome) {
        this.questoesCorretas = questoesCorretas;
        this.questoesRespondidas = questoesRespondidas;
        this.porcentagemAcertoMateria = porcentagemAcertoMateria;
        this.idMateria = idMateria;
        this.nome = nome;
    }
}
