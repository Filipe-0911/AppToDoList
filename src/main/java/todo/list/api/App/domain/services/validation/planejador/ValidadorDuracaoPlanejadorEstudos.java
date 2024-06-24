package todo.list.api.App.domain.services.validation.planejador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import todo.list.api.App.domain.model.PlanejadorEstudos;
import todo.list.api.App.domain.repository.PlanejadorEstudosRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ValidadorDuracaoPlanejadorEstudos implements PlanejadorEstudosValidation {
    @Autowired
    private PlanejadorEstudosRepository repository;

    @Override
    public void validar(PlanejadorEstudos p) {
        LocalDate dataInicio = p.getDataInicio().toLocalDate();
        List<PlanejadorEstudos> listaPlanejadorEstudos = repository.buscaTodosPorDataEUsuarioId(dataInicio, p.getUsuario().getId());
        boolean dataDoNovoPlanejadorEstaCompreendidaEmDataDeOutroPlanejador = isEventInRange(p.getDataInicio(), p.getDataTermino(), listaPlanejadorEstudos);

        if (dataDoNovoPlanejadorEstaCompreendidaEmDataDeOutroPlanejador) {
            throw new RuntimeException("Escolha um horário de início ou término diferente, pois já existe um planejamento que conflita com esses horários.");
        }
    }

    private boolean isEventInRange(LocalDateTime inicioDoNovoEvento, LocalDateTime terminoNovoEvento, List<PlanejadorEstudos> listaPlanejadorEstudos) {
        for (PlanejadorEstudos event : listaPlanejadorEstudos) {
            LocalDateTime planejadorExistenteInicio = event.getDataInicio();
            LocalDateTime planejadorExistenteTermino = event.getDataTermino();

            if (iniciaDuranteUmPlanejadorExistente(inicioDoNovoEvento, planejadorExistenteInicio, planejadorExistenteTermino) ||
                    terminaDuranteUmPlanejadorExistente(terminoNovoEvento, planejadorExistenteInicio, planejadorExistenteTermino) ||
                    planejadorNovoCobreUmPlanejadorExistente(inicioDoNovoEvento, terminoNovoEvento, planejadorExistenteInicio, planejadorExistenteTermino) ||
                    planejadorExistenteCobreNovoPlanejador(inicioDoNovoEvento, terminoNovoEvento, planejadorExistenteInicio, planejadorExistenteTermino)) {
                return true;
            }
        }
        return false;
    }
    private boolean iniciaDuranteUmPlanejadorExistente(LocalDateTime inicioNovoPlanejamento, LocalDateTime inicioDePlanejadorExistente, LocalDateTime terminoPlanejadorExistente) {
        return (inicioNovoPlanejamento.isAfter(inicioDePlanejadorExistente) || inicioNovoPlanejamento.isEqual(inicioDePlanejadorExistente))
                && inicioNovoPlanejamento.isBefore(terminoPlanejadorExistente);
    }

    private boolean terminaDuranteUmPlanejadorExistente(LocalDateTime novoPlanejadorTermino, LocalDateTime planejadorExistenteInicio, LocalDateTime terminoPlanejadorExistente) {
        return novoPlanejadorTermino.isAfter(planejadorExistenteInicio)
                && (novoPlanejadorTermino.isBefore(terminoPlanejadorExistente) || novoPlanejadorTermino.isEqual(terminoPlanejadorExistente));
    }

    private boolean planejadorNovoCobreUmPlanejadorExistente(LocalDateTime novoPlanejadorInicio, LocalDateTime novoPlanejadorTermino, LocalDateTime planejadorExistenteInicio, LocalDateTime terminoPlanejadorExistente) {
        return (novoPlanejadorInicio.isBefore(planejadorExistenteInicio) || novoPlanejadorInicio.isEqual(planejadorExistenteInicio))
                && (novoPlanejadorTermino.isAfter(terminoPlanejadorExistente) || novoPlanejadorTermino.isEqual(terminoPlanejadorExistente));
    }

    private boolean planejadorExistenteCobreNovoPlanejador(LocalDateTime novoPlanejadorInicio, LocalDateTime novoPlanejadorTermino, LocalDateTime planejadorExistenteInicio, LocalDateTime terminoPlanejadorExistente) {
        return (planejadorExistenteInicio.isBefore(novoPlanejadorInicio) || planejadorExistenteInicio.isEqual(novoPlanejadorInicio))
                && (terminoPlanejadorExistente.isAfter(novoPlanejadorTermino) || terminoPlanejadorExistente.isEqual(novoPlanejadorTermino));
    }
}

