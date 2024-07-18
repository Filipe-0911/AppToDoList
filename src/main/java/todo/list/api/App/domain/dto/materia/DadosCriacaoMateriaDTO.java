package todo.list.api.App.domain.dto.materia;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import todo.list.api.App.domain.dto.assunto.DadosCriacaoAsssuntoDTO;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DadosCriacaoMateriaDTO {
    @NotNull
    private String nome = "";
    private List<DadosCriacaoAsssuntoDTO> listaDeAssuntos = new ArrayList<>();

    public String nome() {
        return this.nome;
    }
    public List<DadosCriacaoAsssuntoDTO> listaDeAssuntos() {
        return this.listaDeAssuntos;
    }

    @Override
    public String toString() {
        return "DadosCriacaoMateriaDTO{" +
                "nome='" + nome + '\'' +
                ", listaDeAssuntos=" + listaDeAssuntos +
                '}';
    }
}
