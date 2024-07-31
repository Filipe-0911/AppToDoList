package todo.list.api.App.domain.dto.materia;

import java.util.List;

import todo.list.api.App.domain.dto.assunto.DadosDetalhamentoAssuntoDTO;
import todo.list.api.App.domain.model.Materia;

public record DadosDetalhamentoMateriaDTO(
    Long id,
    String nome,
    List<DadosDetalhamentoAssuntoDTO> listaDeAssuntos,
    String corDaProva
) {
    public DadosDetalhamentoMateriaDTO(Materia materia) {
        this(
            materia.getId(),
            materia.getNome(),
            materia.getListaAssuntos()
                .stream()
                .map(DadosDetalhamentoAssuntoDTO::new)
                .toList(),
                materia.getProva().getHexadecimalCorProva()
        );
    }

}
