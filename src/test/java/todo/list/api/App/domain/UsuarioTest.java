package todo.list.api.App.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import todo.list.api.App.domain.dto.usuario.DadosCriacaoUsuarioDTO;
import todo.list.api.App.domain.model.Usuario;
import todo.list.api.App.domain.repository.UsuarioRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles
public class UsuarioTest {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("Deverá salvar um usuario no banco")
    public void deveraCriarUsuario() {
        Long id = 1L;
        String login = "fulano@xpto.com";
        String nome = "Fulano de Tal";
        String senha = "senha123Xpto@";

        DadosCriacaoUsuarioDTO dadosCriacaoUsuarioDTO = new DadosCriacaoUsuarioDTO(login, nome, senha);
        Usuario usuario = new Usuario(dadosCriacaoUsuarioDTO);
        usuarioRepository.save(usuario);
        boolean usuarioExiste = usuarioRepository.existsByLogin(login);

        assertThat(usuarioExiste).isTrue();

    }
}
