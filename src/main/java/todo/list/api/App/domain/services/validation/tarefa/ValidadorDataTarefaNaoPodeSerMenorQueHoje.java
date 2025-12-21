package todo.list.api.App.domain.services.validation.tarefa;

import org.springframework.stereotype.Component;
import todo.list.api.App.domain.model.Tarefa;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ValidadorDataTarefaNaoPodeSerMenorQueHoje implements TarefaValidation{

    @Override
    public void validar(Tarefa tarefa) {
        LocalDateTime dataDaTarefa = tarefa.getData();
        LocalDateTime dataDeHoje = LocalDateTime.now();
        long duracaoEmSegundos = Duration.between(dataDeHoje, dataDaTarefa).toSeconds();

        if (duracaoEmSegundos < 0) {
            throw new RuntimeException("A data de conclusão da tarefa deve ser posterior à data de hoje!");
        }

    }
}
