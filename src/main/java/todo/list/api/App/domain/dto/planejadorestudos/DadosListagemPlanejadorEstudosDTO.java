package todo.list.api.App.domain.dto.planejadorestudos;

import java.time.LocalDateTime;

import todo.list.api.App.domain.dto.usuario.DadosUsuarioDTO;
import todo.list.api.App.domain.model.PlanejadorEstudos;

public record DadosListagemPlanejadorEstudosDTO(
        LocalDateTime dadaInicio,
        Long assuntoId,
        LocalDateTime dataTermino,
        boolean cancelado,
        Long usuarioId) {

    public DadosListagemPlanejadorEstudosDTO(PlanejadorEstudos planejadorEstudos) {
        this(
                planejadorEstudos.getDataInicio(),
                planejadorEstudos.getAssunto().getId(),
                planejadorEstudos.getDataTermino(),
                planejadorEstudos.isCancelado(),
                new DadosUsuarioDTO(planejadorEstudos.getUsuario()).id()
            );
    }

}
