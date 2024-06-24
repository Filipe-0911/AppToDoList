package todo.list.api.App.domain.services.validation.planejador;

import org.springframework.stereotype.Component;
import todo.list.api.App.domain.model.PlanejadorEstudos;

import java.time.Duration;

@Component
public class ValidaSeDataTerminoIsPosteriorDataInicio implements PlanejadorEstudosValidation{
    @Override
    public void validar(PlanejadorEstudos p) {
        long diferentaEmMinutos = Duration.between(p.getDataInicio(), p.getDataTermino()).toMinutes();
        if (diferentaEmMinutos < 0) {
            throw new RuntimeException("A Data de término deve ser posterior à data de início.");
        }
    }
}
