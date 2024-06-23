package todo.list.api.App.domain.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name="planejador_estudos")
@Entity(name="PlanejadorEstudos")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class PlanejadorEstudos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    private LocalDateTime dataInicio;

    @Setter
    @ManyToOne(cascade = CascadeType.REMOVE)
    private Assunto assunto;
    @Setter
    private LocalDateTime dataTermino;
    
    @Setter
    private boolean cancelado;

    @Setter
    @ManyToOne
    private Usuario usuario;
}
