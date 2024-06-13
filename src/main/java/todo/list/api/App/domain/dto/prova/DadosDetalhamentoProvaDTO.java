package todo.list.api.App.domain.dto.prova;

import java.time.LocalDateTime;
import java.util.List;

import todo.list.api.App.domain.dto.materia.DadosDetalhamentoMateriaDTO;
import todo.list.api.App.domain.model.Prova;


public record DadosDetalhamentoProvaDTO(
    Long id,
    String titulo,
    LocalDateTime data,
    List<DadosDetalhamentoMateriaDTO> listaDeMaterias
) {

    public DadosDetalhamentoProvaDTO(Prova prova) {
        this(
            prova.getId(), 
            prova.getTitulo(), 
            prova.getDataDaProva(), 
            prova.getListaDeMaterias().stream()
                .map(DadosDetalhamentoMateriaDTO::new)
                .toList()
            );
    }

}
