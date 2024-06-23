package todo.list.api.App.domain.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

@Service
public class PlanejadorEstudosService {

    @Autowired
    private PlanejadorEstudosRepository planejadorEstudosRepository;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private AssuntoService assuntoService;

    public ResponseEntity<DadosDetalhamentoPlanejadorEstudosDTO> alteraPlanejamento(Long idPlanejador, HttpServletRequest request, DadosAlteracaoPlanejadorEstudosDTO dadosAlteracaoPlanejadorEstudosDTO) {
        Usuario usuario = usuarioService.buscaUsuario(request);
        PlanejadorEstudos planejadorEstudos = planejadorEstudosRepository.getReferenceById(idPlanejador);

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
            return ResponseEntity.ok(new DadosDetalhamentoPlanejadorEstudosDTO(planejadorEstudos));

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
                    .map(p -> new DadosListagemPlanejadorEstudosDTO(p));

            return ResponseEntity.ok(listaDePlanejadorEstudos);
        }
        return ResponseEntity.badRequest().build();
    }

    public ResponseEntity<DadosListagemPlanejadorEstudosDTO> inserePlanejadorEstudos(Long idAssunto, @Valid DadosCriacaoPlanejadorEstudosDTO dadosCriacaoPlanejadorEstudosDTO, HttpServletRequest request) {
        Usuario usuario = usuarioService.buscaUsuario(request);
        Assunto assunto = assuntoService.buscarAssuntoEspecificoSemParametrosDePath(idAssunto);
        Materia materia = assunto.getMateria();
        if (usuarioService.verificaSeMateriaPertenceAUsuario(usuario, materia)) {
            PlanejadorEstudos planejadorEstudos = new PlanejadorEstudos(null,
                    dadosCriacaoPlanejadorEstudosDTO.dataInicio(),
                    assunto,
                    dadosCriacaoPlanejadorEstudosDTO.dataTermino(),
                    false,
                    usuario);

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
}
