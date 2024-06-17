package todo.list.api.App.domain.dto.materia;

import java.util.List;

import todo.list.api.App.domain.dto.assunto.DadosListagemAssuntoDTO;
import todo.list.api.App.domain.model.Materia;

public record DadosListagemMateriaDTO(
    Long id,
    String nome,
    String tituloProva,
    List<DadosListagemAssuntoDTO> listaAssuntos
) {
    public DadosListagemMateriaDTO(Materia materia) {
        this(
            materia.getId(),
            materia.getNome(), 
            materia.getProva().getTitulo(), 
            materia.getListaAssuntos()
                .stream()
                .map(DadosListagemAssuntoDTO::new)
                .toList()
            );
    }

}
