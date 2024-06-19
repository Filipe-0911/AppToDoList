package todo.list.api.App.domain.dto.planejadorestudos;

import java.time.LocalDateTime;

import todo.list.api.App.domain.dto.assunto.DadosListagemAssuntoDTO;
import todo.list.api.App.domain.dto.usuario.DadosUsuarioDTO;

public record DadosListagemPlanejadorEstudosDTO(
    LocalDateTime dadaInicio,
    DadosListagemAssuntoDTO assunto,
    LocalDateTime dataTermino,
    boolean cancelado,
    DadosUsuarioDTO usuario
) {

}
