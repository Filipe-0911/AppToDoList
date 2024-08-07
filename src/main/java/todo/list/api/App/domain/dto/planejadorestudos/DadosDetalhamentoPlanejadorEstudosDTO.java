package todo.list.api.App.domain.dto.planejadorestudos;

import java.time.LocalDateTime;

import todo.list.api.App.domain.model.PlanejadorEstudos;

public record DadosDetalhamentoPlanejadorEstudosDTO(
    Long id,
    LocalDateTime dataInicio,

    LocalDateTime dataTermino,
    boolean isCancelado,
    Long idUsuario
) {
    public DadosDetalhamentoPlanejadorEstudosDTO(PlanejadorEstudos p) {
        this(
                p.getId(),
                p.getDataInicio(),

                p.getDataTermino(),
                p.isCancelado(),
                p.getUsuario().getId()
        );
    }

}
