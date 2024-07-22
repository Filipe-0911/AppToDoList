package todo.list.api.App.domain.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import todo.list.api.App.domain.services.DateTimeConverterFromString;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import todo.list.api.App.domain.dto.planejadorestudos.DadosAlteracaoPlanejadorEstudosDTO;
import todo.list.api.App.domain.dto.planejadorestudos.DadosCriacaoPlanejadorEstudosDTO;
import todo.list.api.App.domain.dto.planejadorestudos.DadosDetalhamentoPlanejadorEstudosDTO;
import todo.list.api.App.domain.dto.planejadorestudos.DadosListagemPlanejadorEstudosDTO;
import todo.list.api.App.domain.model.Assunto;
import todo.list.api.App.domain.model.Materia;
import todo.list.api.App.domain.model.PlanejadorEstudos;
import todo.list.api.App.domain.model.Usuario;
import todo.list.api.App.domain.repository.PlanejadorEstudosRepository;
import todo.list.api.App.domain.services.validation.planejador.PlanejadorEstudosValidation;

@Service
public class PlanejadorEstudosService {

    @Autowired
    private PlanejadorEstudosRepository planejadorEstudosRepository;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private AssuntoService assuntoService;
    @Autowired
    private List<PlanejadorEstudosValidation> validadorPlanejadorEstudos;

    public ResponseEntity<DadosListagemPlanejadorEstudosDTO> alteraPlanejamento(Long idPlanejador, HttpServletRequest request, DadosAlteracaoPlanejadorEstudosDTO dadosAlteracaoPlanejadorEstudosDTO) {
        Usuario usuario = usuarioService.buscaUsuario(request);
        PlanejadorEstudos planejadorEstudos = planejadorEstudosRepository.getReferenceById(idPlanejador);

        LocalDateTime dataInicio = dadosAlteracaoPlanejadorEstudosDTO.dataInicio();
        LocalDateTime dataTermino = dadosAlteracaoPlanejadorEstudosDTO.dataTermino();

        PlanejadorEstudos planejadorEstudosAlteracoes = new PlanejadorEstudos(
                null,
                dataInicio,
                planejadorEstudos.getAssunto(),
                dataTermino,
                false,
                usuario
        );

        validadorPlanejadorEstudos.forEach(v -> v.validar(planejadorEstudosAlteracoes));
        boolean planejadorPertenceAUsuario = usuarioService.verificaSePlanejadorEstudosPertenceAUsuario(usuario, planejadorEstudos);
        if (planejadorPertenceAUsuario) {

            if (dadosAlteracaoPlanejadorEstudosDTO.dataInicio() != null) {
                planejadorEstudos.setDataInicio(dadosAlteracaoPlanejadorEstudosDTO.dataInicio());
            }
            if (dadosAlteracaoPlanejadorEstudosDTO.dataTermino() != null) {
                planejadorEstudos.setDataTermino(dadosAlteracaoPlanejadorEstudosDTO.dataTermino());
            }
            if (dadosAlteracaoPlanejadorEstudosDTO.idAssunto() != null) {
                Assunto assunto = assuntoService.buscarAssuntoEspecificoSemParametrosDePath(dadosAlteracaoPlanejadorEstudosDTO.idAssunto());
                planejadorEstudos.setAssunto(assunto);
            }
            return ResponseEntity.ok(new DadosListagemPlanejadorEstudosDTO(planejadorEstudos));

        }
        return ResponseEntity.badRequest().build();
    }

    public ResponseEntity<Void> deletaPlanejamento(Long idPlanejador, HttpServletRequest request) {
        Usuario usuario = usuarioService.buscaUsuario(request);
        List<PlanejadorEstudos> listaPlanejadorEstudosUsuario = usuario.getPlanejadorEstudos();
        PlanejadorEstudos planejadorEstudos = planejadorEstudosRepository.getReferenceById(idPlanejador);
        Assunto assunto = planejadorEstudos.getAssunto();

        boolean planejadorPertenceAUsuario = listaPlanejadorEstudosUsuario.contains(planejadorEstudos);

        if (planejadorPertenceAUsuario) {
            planejadorEstudosRepository.delete(planejadorEstudos);
            usuario.deletaPlanejamento(planejadorEstudos);
            assunto.deletaPlanejadorEstudos(planejadorEstudos);

            return ResponseEntity.noContent().build();

        }
        return ResponseEntity.badRequest().build();
    }

    public ResponseEntity<DadosDetalhamentoPlanejadorEstudosDTO> buscaPlanejadorEspecifico(Long idPlanejador, HttpServletRequest request) {
        Usuario usuario = usuarioService.buscaUsuario(request);
        PlanejadorEstudos planejadorEstudos = planejadorEstudosRepository.getReferenceById(idPlanejador);
        List<PlanejadorEstudos> listaPlanejadorEstudosUsuario = usuario.getPlanejadorEstudos();

        boolean planejadorPertenceAUsuario = listaPlanejadorEstudosUsuario.contains(planejadorEstudos);
        if (planejadorPertenceAUsuario) {

            return ResponseEntity.ok(new DadosDetalhamentoPlanejadorEstudosDTO(planejadorEstudos));
        }

        return ResponseEntity.badRequest().build();
    }

    public ResponseEntity<Page<DadosListagemPlanejadorEstudosDTO>> listaPlanejador(@PageableDefault(size = 5, page = 0, sort = {"dataInicio"}) Pageable pageable, Long idAssunto, HttpServletRequest request) {
        Usuario usuario = usuarioService.buscaUsuario(request);
        Assunto assunto = assuntoService.buscarAssuntoEspecificoSemParametrosDePath(idAssunto);
        Materia materia = assunto.getMateria();

        if (usuarioService.verificaSeMateriaPertenceAUsuario(usuario, materia)) {
            Page<DadosListagemPlanejadorEstudosDTO> listaDePlanejadorEstudos = planejadorEstudosRepository.findAllByAssuntoIdAndCanceladoIsFalse(pageable, idAssunto)
                    .map(DadosListagemPlanejadorEstudosDTO::new);

            return ResponseEntity.ok(listaDePlanejadorEstudos);
        }
        return ResponseEntity.badRequest().build();
    }

    public ResponseEntity<DadosListagemPlanejadorEstudosDTO> inserePlanejadorEstudos(Long idAssunto, @Valid DadosCriacaoPlanejadorEstudosDTO dadosCriacaoPlanejadorEstudosDTO, HttpServletRequest request) {
        Usuario usuario = usuarioService.buscaUsuario(request);
        Assunto assunto = assuntoService.buscarAssuntoEspecificoSemParametrosDePath(idAssunto);
        Materia materia = assunto.getMateria();

        if (usuarioService.verificaSeMateriaPertenceAUsuario(usuario, materia)) {
            LocalDateTime dataInicio = dadosCriacaoPlanejadorEstudosDTO.getDataInicio();
            LocalDateTime dataTermino = dadosCriacaoPlanejadorEstudosDTO.getDataTermino();

            PlanejadorEstudos planejadorEstudos = new PlanejadorEstudos(
                    null,
                    dataInicio,
                    assunto,
                    dataTermino,
                    false,
                    usuario
            );
            validadorPlanejadorEstudos.forEach(v -> v.validar(planejadorEstudos));

            planejadorEstudosRepository.save(planejadorEstudos);
            usuario.setPlanejadorEstudos(planejadorEstudos);
            assunto.setPlanejadorEstudos(planejadorEstudos);

            return ResponseEntity.ok(new DadosListagemPlanejadorEstudosDTO(planejadorEstudos));

        }
        return ResponseEntity.badRequest().build();
    }

    public ResponseEntity<DadosDetalhamentoPlanejadorEstudosDTO> cancelarPlanejamentoEstudos(Long idPlanejador, HttpServletRequest request) {
        Usuario usuario = usuarioService.buscaUsuario(request);

        PlanejadorEstudos planejadorEstudos = planejadorEstudosRepository.getReferenceById(idPlanejador);
        boolean planejadorAssuntoPertenceAUsuario = usuarioService.verificaSePlanejadorEstudosPertenceAUsuario(usuario, planejadorEstudos);

        if (planejadorAssuntoPertenceAUsuario) {
            planejadorEstudos.setCancelado(true);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }

    public ResponseEntity<Page<DadosListagemPlanejadorEstudosDTO>> listarTodosOsPlanejadoresDoUsuario(Pageable pageable, HttpServletRequest request) {
        Usuario usuario = usuarioService.buscaUsuario(request);
        List<DadosListagemPlanejadorEstudosDTO> listaDePlanejadores = usuario.getPlanejadorEstudos().stream().map(DadosListagemPlanejadorEstudosDTO::new).toList();
        Page<DadosListagemPlanejadorEstudosDTO> pagePlanejador = new PageImpl<DadosListagemPlanejadorEstudosDTO>(listaDePlanejadores);
        return ResponseEntity.ok(pagePlanejador);

    }

}
