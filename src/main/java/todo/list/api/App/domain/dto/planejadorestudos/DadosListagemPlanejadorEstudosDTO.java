package todo.list.api.App.domain.dto.planejadorestudos;

import java.time.LocalDateTime;

import todo.list.api.App.domain.model.PlanejadorEstudos;

public record DadosListagemPlanejadorEstudosDTO(
        Long id,
        LocalDateTime dataInicio,
        Long assuntoId,
        Long idMateria,
        Long idProva,
        LocalDateTime dataTermino,
        String nomeAssunto,
        Long usuarioId,
        String cor) {

    public DadosListagemPlanejadorEstudosDTO(PlanejadorEstudos planejadorEstudos) {
        this(
                planejadorEstudos.getId(),
                planejadorEstudos.getDataInicio(),
                planejadorEstudos.getAssunto().getId(),
                planejadorEstudos.getAssunto().getMateria().getId(),
                planejadorEstudos.getAssunto().getMateria().getProva().getId(),
                planejadorEstudos.getDataTermino(),
                planejadorEstudos.getAssunto().getNome(),
                planejadorEstudos.getUsuario().getId(),
                planejadorEstudos.getAssunto().getMateria().getProva().getHexadecimalCorProva()
            );
    }

}
