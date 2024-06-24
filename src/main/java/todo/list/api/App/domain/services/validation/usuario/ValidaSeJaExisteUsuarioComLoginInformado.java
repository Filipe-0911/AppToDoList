package todo.list.api.App.domain.services.validation.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import todo.list.api.App.domain.dto.usuario.DadosCriacaoUsuarioDTO;
import todo.list.api.App.domain.repository.UsuarioRepository;

@Component
public class ValidaSeJaExisteUsuarioComLoginInformado implements UsuarioValidation {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Override
    public void validar(DadosCriacaoUsuarioDTO dadosCriacaoUsuarioDTO) {
        boolean existeUmUsuarioComEsteLogin = usuarioRepository.existsByLogin(dadosCriacaoUsuarioDTO.login());
        if (existeUmUsuarioComEsteLogin) {
            throw new RuntimeException("Login indisponível.");
        }
    }
}
