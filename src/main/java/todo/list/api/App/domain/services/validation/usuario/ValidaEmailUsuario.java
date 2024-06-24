package todo.list.api.App.domain.services.validation.usuario;

import org.springframework.stereotype.Component;
import todo.list.api.App.domain.dto.usuario.DadosCriacaoUsuarioDTO;

@Component
public class ValidaEmailUsuario implements UsuarioValidation{
    @Override
    public void validar(DadosCriacaoUsuarioDTO dadosCriacaoUsuarioDTO) {
        if (!isValidEmail(dadosCriacaoUsuarioDTO.login())) {
            throw new RuntimeException("O Login deve ser um email válido: " + dadosCriacaoUsuarioDTO.login());
        }
    }
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email != null && email.matches(emailRegex);
    }
}
