package todo.list.api.App.domain.dto.materia;

import todo.list.api.App.domain.model.Materia;

public record DadosDetalhamentoMateriaDTO(
    Long id,
    String nome
) {
    public DadosDetalhamentoMateriaDTO(Materia materia) {
        this(
            materia.getId(),
            materia.getNome()
        );
    }

}
