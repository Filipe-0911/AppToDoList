package todo.list.api.App.domain.dto.materia;

import todo.list.api.App.domain.model.Materia;
import todo.list.api.App.domain.model.Prova;

public record DadosListagemMateria(
    String nome,
    Prova prova
    //List<Questao> questoes;
) {
    public DadosListagemMateria(Materia materia) {
        this(materia.getNome(), materia.getProva());
    }

}
