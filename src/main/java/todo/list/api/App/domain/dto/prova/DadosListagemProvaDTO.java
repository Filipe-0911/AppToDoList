package todo.list.api.App.domain.dto.prova;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import todo.list.api.App.domain.dto.materia.DadosListagemMateria;
import todo.list.api.App.domain.model.Prova;

public record DadosListagemProvaDTO(
    String titulo,
    LocalDateTime dataDaProva,
    List<DadosListagemMateria> listaDeMaterias
) {

    public DadosListagemProvaDTO(Prova prova) {
        this(prova.getTitulo(), 
        prova.getDataDaProva(), 
        prova.getListaDeMaterias().stream()
        .map(DadosListagemMateria::new).
        collect(Collectors.toList()));
    }

}
