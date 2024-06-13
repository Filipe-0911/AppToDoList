package todo.list.api.App.domain.dto.materia;

import java.util.List;

import todo.list.api.App.domain.dto.questao.DadosListagemQuestoesDTO;
import todo.list.api.App.domain.model.Materia;

public record DadosDetalhamentoMateriaDTO(
    Long id,
    String nome,
    List<DadosListagemQuestoesDTO> listaQuestoes
) {
    public DadosDetalhamentoMateriaDTO(Materia materia) {
        this(
            materia.getId(),
            materia.getNome(),
            materia.getListaQuestoes()
                .stream()
                .map(DadosListagemQuestoesDTO::new)
                .toList()
        );
    }

}
