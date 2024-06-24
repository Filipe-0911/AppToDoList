package todo.list.api.App.domain.services.validation.usuario;

import todo.list.api.App.domain.dto.usuario.DadosCriacaoUsuarioDTO;

public interface UsuarioValidation {
    void validar(DadosCriacaoUsuarioDTO dadosCriacaoUsuarioDTO);
}
