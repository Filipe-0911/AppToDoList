package todo.list.api.App.domain.dto.planejadorestudos;

import java.time.LocalDateTime;

import todo.list.api.App.domain.dto.usuario.DadosUsuarioDTO;
import todo.list.api.App.domain.model.PlanejadorEstudos;

public record DadosListagemPlanejadorEstudosDTO(
        Long id,
        LocalDateTime dataInicio,
        Long assuntoId,
        Long idMateria,
        Long idProva,
        LocalDateTime dataTermino,
        String nomeAssunto,
        Long usuarioId) {

    public DadosListagemPlanejadorEstudosDTO(PlanejadorEstudos planejadorEstudos) {
        this(
                planejadorEstudos.getId(),
                planejadorEstudos.getDataInicio(),
                planejadorEstudos.getAssunto().getId(),
                planejadorEstudos.getAssunto().getMateria().getId(),
                planejadorEstudos.getAssunto().getMateria().getProva().getId(),
                planejadorEstudos.getDataTermino(),
                planejadorEstudos.getAssunto().getNome(),
                new DadosUsuarioDTO(planejadorEstudos.getUsuario()).id()
            );
    }

}
