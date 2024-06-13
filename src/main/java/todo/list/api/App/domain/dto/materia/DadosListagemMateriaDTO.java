package todo.list.api.App.domain.dto.materia;

import java.util.List;

import todo.list.api.App.domain.dto.questao.DadosListagemQuestoesDTO;
import todo.list.api.App.domain.model.Materia;

public record DadosListagemMateriaDTO(
    Long id,
    String nome,
    String tituloProva,
    List<DadosListagemQuestoesDTO> listaQuestoes
) {
    public DadosListagemMateriaDTO(Materia materia) {
        this(
            materia.getId(),
            materia.getNome(), 
            materia.getProva().getTitulo(), 
            materia.getListaQuestoes().stream()
            .map(DadosListagemQuestoesDTO::new)
            .toList()
            );
    }

}
