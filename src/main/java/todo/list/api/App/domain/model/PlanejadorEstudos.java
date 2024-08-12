package todo.list.api.App.domain.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import todo.list.api.App.domain.dto.planejadorestudos.DadosCriacaoPlanejadorEstudosDTO;

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

    public PlanejadorEstudos(DadosCriacaoPlanejadorEstudosDTO dadosCriacaoPlanejadorEstudosDTO, Assunto assunto, Usuario usuario) {
        this.dataInicio = dadosCriacaoPlanejadorEstudosDTO.getDataInicio();
        this.dataTermino = dadosCriacaoPlanejadorEstudosDTO.getDataTermino();
        this.assunto = assunto;
        this.cancelado = false;
        this.usuario = usuario;
    }

}
