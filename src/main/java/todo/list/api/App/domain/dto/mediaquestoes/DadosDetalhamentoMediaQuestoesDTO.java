package todo.list.api.App.domain.dto.mediaquestoes;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DadosDetalhamentoMediaQuestoesDTO {
    private Date dataPreenchimento;
    private String nome;
    private Long questoesFeitas;
    private Long questoesCorretas;
    private Double porcentagem;

}
