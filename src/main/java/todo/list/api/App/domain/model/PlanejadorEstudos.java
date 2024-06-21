package todo.list.api.App.domain.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name="palenajor_estudos")
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
    @ManyToOne
    private Assunto assunto;
    @Setter
    private LocalDateTime dataTermino;
    
    @Setter
    private boolean cancelado;

    @Setter
    @ManyToOne
    private Usuario usuario;
    

}
