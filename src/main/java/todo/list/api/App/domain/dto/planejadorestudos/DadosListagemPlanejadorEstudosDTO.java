package todo.list.api.App.domain.dto.planejadorestudos;

import java.time.LocalDateTime;

import todo.list.api.App.domain.dto.usuario.DadosUsuarioDTO;
import todo.list.api.App.domain.model.PlanejadorEstudos;

public record DadosListagemPlanejadorEstudosDTO(
        Long id,
        LocalDateTime dadaInicio,
        Long assuntoId,
        LocalDateTime dataTermino,
        Long usuarioId) {

    public DadosListagemPlanejadorEstudosDTO(PlanejadorEstudos planejadorEstudos) {
        this(
                planejadorEstudos.getId(),
                planejadorEstudos.getDataInicio(),
                planejadorEstudos.getAssunto().getId(),
                planejadorEstudos.getDataTermino(),
                new DadosUsuarioDTO(planejadorEstudos.getUsuario()).id()
            );
    }

}
