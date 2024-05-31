package todo.list.api.App.domain.model;

import jakarta.persistence.*;
import lombok.*;
import todo.list.api.App.domain.dto.tarefa.DadosCriacaoTarefasDTO;
import todo.list.api.App.domain.services.DateTimeConverterFromString;

import java.time.LocalDateTime;

@Table(name = "tarefas")
@Entity(name = "Tarefa")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Tarefa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String descricao;
    private LocalDateTime data;

    @Setter
    @ManyToOne
    private Usuario usuario;

    public Tarefa(DadosCriacaoTarefasDTO dados) {
        this.titulo = dados.titulo();
        this.descricao = dados.descricao();
        this.data = DateTimeConverterFromString.parse(dados.data());
    }

}
