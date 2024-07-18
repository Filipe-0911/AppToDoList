package todo.list.api.App.domain.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import todo.list.api.App.domain.dto.usuario.DadosCriacaoUsuarioDTO;
import todo.list.api.App.domain.services.UsuarioService;

@Table(name = "usuarios")
@Entity(name = "Usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String nome;
    private String senha;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Tarefa> tarefas = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Prova> provas = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PlanejadorEstudos> planejadorEstudos = new ArrayList<>();

    public Usuario(DadosCriacaoUsuarioDTO dados) {
        this.login = dados.login();
        this.nome = dados.nome();
        this.senha = UsuarioService.encriptadorSenhaUsuario(dados.senha());
    }

    public void setTarefas(Tarefa tarefa) {
        this.tarefas.add(tarefa);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setProvas(Prova prova) {
        this.provas.add(prova);
    }

    public void setPlanejadorEstudos(PlanejadorEstudos planejadorEstudos) {
        this.planejadorEstudos.add(planejadorEstudos);
    }
    
    public void deleteProvas(Prova prova) {
        this.provas = provas.stream()
            .filter(p -> !Objects.equals(p.getId(), prova.getId()))
            .toList();
    }
    public void deletaPlanejamento(PlanejadorEstudos planejadorEstudos) {
        this.planejadorEstudos.remove(planejadorEstudos);
    }

    public void removerTodosOsPlanejamentosDaProva(Prova prova) {
        this.planejadorEstudos = this.planejadorEstudos.stream()
                .filter(p -> p.getAssunto().getMateria().getProva() != prova).toList();
    }

    public void removerTodosOsPlanejamentosDoAssunto(Assunto assunto) {
        this.planejadorEstudos = this.planejadorEstudos.stream()
               .filter(p -> !p.getAssunto().equals(assunto)).toList();
    }

    public void removerTodosOsPlanejamentosDaMateria(Materia materia) {
        this.planejadorEstudos = this.planejadorEstudos.stream()
               .filter(p -> !p.getAssunto().getMateria().equals(materia)).toList();
    }

    public void deleteTarefa(Tarefa tarefaEspecifica) {
        this.tarefas.remove(tarefaEspecifica);
    }
}
