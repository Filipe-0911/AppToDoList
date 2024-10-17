package todo.list.api.App.domain.dto.mediaquestoes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MediaQuestoesPorAssuntoDTO {
    private Long idAssunto;
    private String nome;
    private Long questoesFeitas;
    private Long questoesCorretas;
    private Double porcentagem;
}
