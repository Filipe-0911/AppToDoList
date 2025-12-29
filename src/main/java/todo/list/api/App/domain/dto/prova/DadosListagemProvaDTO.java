package todo.list.api.App.domain.dto.prova;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import todo.list.api.App.domain.dto.materia.DadosListagemMateriaDTO;
import todo.list.api.App.domain.model.Prova;
import todo.list.api.App.domain.services.DataService;

public record DadosListagemProvaDTO(
        Long id,
        String titulo,
        LocalDateTime dataDaProva,
        String corDaProva,
        int numeroDeDiasAteAProva,
        List<DadosListagemMateriaDTO> listaDeMaterias
) {

    public DadosListagemProvaDTO(Prova prova) {
        this(
                prova.getId(),
                prova.getTitulo(),
                prova.getDataDaProva(),
                prova.getHexadecimalCorProva(),
                DataService.calculaDiasEntreDatas(LocalDateTime.now(), prova.getDataDaProva()),
                prova.getListaDeMaterias().stream()
                        .map(DadosListagemMateriaDTO::new).
                        collect(Collectors.toList())
        );
    }

}
