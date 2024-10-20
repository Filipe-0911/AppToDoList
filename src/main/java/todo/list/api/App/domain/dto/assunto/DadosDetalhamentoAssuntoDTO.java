package todo.list.api.App.domain.dto.assunto;

import java.util.List;

import todo.list.api.App.domain.dto.mediaquestoes.MediaQuestoesPorAssuntoDTO;
import todo.list.api.App.domain.model.Assunto;
import todo.list.api.App.domain.model.EstatisticaQuestao;

public record DadosDetalhamentoAssuntoDTO(
        Long id,
        Long idMateria,
        Long idProva,
        String nome,
        int quantidadePdf,
        String comentarios,
        List<Long> idQuestoes,
        MediaQuestoesPorAssuntoDTO estatisticas

) {

    // Construtor para criar o DTO de Assunto a partir de um objeto Assunto
    public DadosDetalhamentoAssuntoDTO(Assunto assunto) {
        this(
                assunto.getId(),
                assunto.getMateria().getId(),
                assunto.getMateria().getProva().getId(),
                assunto.getNome(),
                assunto.getQuantidadePdf(),
                assunto.getComentarios(),
                assunto.getListaDeEstatisticaQuestoes()
                        .stream()
                        .map(EstatisticaQuestao::getId)
                        .toList(),
                null
        );
    }

    // Novo construtor para preencher também as estatísticas
    public DadosDetalhamentoAssuntoDTO(Assunto assunto, MediaQuestoesPorAssuntoDTO mediaQuestoes) {
        this(
                assunto.getId(),
                assunto.getMateria().getId(),
                assunto.getMateria().getProva().getId(),
                assunto.getNome(),
                assunto.getQuantidadePdf(),
                assunto.getComentarios(),
                assunto.getListaDeEstatisticaQuestoes()
                        .stream()
                        .map(EstatisticaQuestao::getId)
                        .toList(),
                mediaQuestoes
        );
    }
}
