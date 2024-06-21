package todo.list.api.App.domain.dto.assunto;

import io.micrometer.common.lang.Nullable;

public record DadosAlteracaoAssuntoDTO(
    @Nullable
    String nome,
    int quantidadeDePdf 
) {

}
