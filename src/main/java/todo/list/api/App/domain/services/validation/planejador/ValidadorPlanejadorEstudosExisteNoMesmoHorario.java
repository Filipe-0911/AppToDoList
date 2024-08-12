package todo.list.api.App.domain.services.validation.planejador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import todo.list.api.App.domain.model.PlanejadorEstudos;
import todo.list.api.App.domain.repository.PlanejadorEstudosRepository;

@Component
public class ValidadorPlanejadorEstudosExisteNoMesmoHorario implements PlanejadorEstudosValidation {
    @Autowired
    private PlanejadorEstudosRepository planejadorEstudosRepository;
    @Override
    public void validar(PlanejadorEstudos planejadorEstudos) {
        boolean existeOutroPlanejadorEstudosNoMesmoHorario = planejadorEstudosRepository.verificaSeExisteUmPlanejadorDiferenteParaOHorarioProposto(planejadorEstudos.getDataInicio(), planejadorEstudos.getUsuario().getId(), planejadorEstudos.getId());
        if (existeOutroPlanejadorEstudosNoMesmoHorario) {
            throw new RuntimeException("Já existe um planejador para este dia e horário.");
        }

    }
}
