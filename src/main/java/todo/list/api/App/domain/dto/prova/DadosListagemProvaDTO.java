package todo.list.api.App.domain.dto.prova;

import java.time.LocalDateTime;

import todo.list.api.App.domain.model.Prova;

public record DadosListagemProvaDTO(
    String titulo,
    LocalDateTime dataDaProva
) {

    public DadosListagemProvaDTO(Prova prova) {
        this(prova.getTitulo(), prova.getDataDaProva());
    }

}
