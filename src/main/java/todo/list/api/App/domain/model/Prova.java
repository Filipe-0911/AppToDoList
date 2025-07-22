package todo.list.api.App.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import todo.list.api.App.domain.dto.prova.DadosCriacaoProvaDTO;

@Table(name="provas")
@Entity(name="Prova")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Prova {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    private String titulo;
    @Setter
    private LocalDateTime dataDaProva;

    @Setter
    @Column(name = "cor_prova")
    private String hexadecimalCorProva;

    @Setter
    @ManyToOne
    private Usuario usuario;

    @OneToMany(mappedBy = "prova", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Materia> listaDeMaterias = new ArrayList<>();

    public void setListaDeMaterias (Materia materia) {
        this.listaDeMaterias.add(materia);
    }

    public Prova(@Valid DadosCriacaoProvaDTO dadosProva) {
        this.titulo = dadosProva.titulo();
        this.dataDaProva = dadosProva.dataDaProva();
        this.hexadecimalCorProva = dadosProva.corDaProva();

    }

    public void removeMateria(Materia materia) {
        this.listaDeMaterias = this.listaDeMaterias.stream()
            .filter(materiaDaLista -> !materiaDaLista.equals(materia)).toList();
    }
}
