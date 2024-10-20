package todo.list.api.App.domain.dto.prova;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import todo.list.api.App.domain.dto.assunto.DadosDetalhamentoAssuntoDTO;
import todo.list.api.App.domain.dto.materia.DadosDetalhamentoMateriaDTO;
import todo.list.api.App.domain.dto.mediaquestoes.MediaQuestoesPorAssuntoDTO;
import todo.list.api.App.domain.model.Prova;
import todo.list.api.App.domain.repository.EstatisticaQuestaoRepository;


public record DadosDetalhamentoProvaDTO(
    Long id,
    String titulo,
    LocalDateTime data,
    String corDaProva,
    List<DadosDetalhamentoMateriaDTO> listaDeMaterias
) {

    public DadosDetalhamentoProvaDTO(Prova prova) {
        this(
            prova.getId(), 
            prova.getTitulo(), 
            prova.getDataDaProva(),
            prova.getHexadecimalCorProva(),
            prova.getListaDeMaterias().stream()
                .map(DadosDetalhamentoMateriaDTO::new)
                .toList()
            );
    }

    public DadosDetalhamentoProvaDTO(Prova prova, EstatisticaQuestaoRepository estatisticaQuestaoRepository) {
        this(
                prova.getId(),
                prova.getTitulo(),
                prova.getDataDaProva(),
                prova.getHexadecimalCorProva(),
                prova.getListaDeMaterias().stream()
                        .map(materia -> new DadosDetalhamentoMateriaDTO(
                                materia.getId(),
                                materia.getNome(),
                                materia.getListaAssuntos().stream()
                                        .map(assunto -> {
                                            // Busca as estatísticas de média de questões para o assunto
                                            Optional<MediaQuestoesPorAssuntoDTO> mediaQuestoes = estatisticaQuestaoRepository.buscaMediaQuestoesPorAssunto(assunto.getId());

                                            // Cria o DTO do Assunto com ou sem estatísticas
                                            return mediaQuestoes
                                                    .map(media -> new DadosDetalhamentoAssuntoDTO(assunto, media))
                                                    .orElseGet(() -> new DadosDetalhamentoAssuntoDTO(assunto));
                                        })
                                        .toList(),
                                materia.getProva().getHexadecimalCorProva()
                        ))
                        .toList()
        );
    }


}
