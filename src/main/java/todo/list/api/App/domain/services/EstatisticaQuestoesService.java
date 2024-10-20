package todo.list.api.App.domain.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import todo.list.api.App.domain.dto.mediaquestoes.DadosDetalhamentoMediaQuestoesDTO;
import todo.list.api.App.domain.dto.estatistica_questao.DadosCriacaoEstatisticaQuestaoDTO;
import todo.list.api.App.domain.dto.estatistica_questao.DadosDetalhamentoEstatisticaQuestaoDTO;
import todo.list.api.App.domain.dto.estatistica_questao.DadosListagemEstatisticaQuestoesDTO;
import todo.list.api.App.domain.dto.mediaquestoes.MediaQuestoesPorAssuntoDTO;
import todo.list.api.App.domain.dto.mediaquestoes.MediaQuestoesPorMateriaDTO;
import todo.list.api.App.domain.model.Assunto;
import todo.list.api.App.domain.model.Materia;
import todo.list.api.App.domain.model.Prova;
import todo.list.api.App.domain.model.EstatisticaQuestao;
import todo.list.api.App.domain.model.Usuario;
import todo.list.api.App.domain.repository.EstatisticaQuestaoRepository;

@Service
public class EstatisticaQuestoesService {
    @Autowired
    private EstatisticaQuestaoRepository estatisticaQuestaoRepository;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ProvaService provaService;
    @Autowired
    private MateriaService materiaService;
    @Autowired
    private AssuntoService assuntoService;

    public ResponseEntity<DadosDetalhamentoEstatisticaQuestaoDTO> adicionarEstatisticaQuestao(Long idMateria, Long idAssunto, HttpServletRequest request, DadosCriacaoEstatisticaQuestaoDTO dadosCriacaoEstatisticaQuestaoDTO) {
        Assunto assunto = assuntoService.buscarAssuntoEspecificoSemParametrosDePath(idAssunto);

        if (materiaPertenceAoUsuario(idMateria, request)) {
            EstatisticaQuestao estatisticaQuestaoBuscada = estatisticaQuestaoRepository.findByDataPreenchimentoAndAssuntoId(dadosCriacaoEstatisticaQuestaoDTO.dataPreenchimento(), assunto.getId());
            if (estatisticaQuestaoBuscada != null && assunto.getId().equals(estatisticaQuestaoBuscada.getAssunto().getId())) {
                estatisticaQuestaoBuscada.setQuestoesAcertadas(dadosCriacaoEstatisticaQuestaoDTO.questoesAcertadas());
                estatisticaQuestaoBuscada.setQuestoesFeitas(dadosCriacaoEstatisticaQuestaoDTO.questoesFeitas());
                return ResponseEntity.ok(new DadosDetalhamentoEstatisticaQuestaoDTO(estatisticaQuestaoBuscada));
            }
            EstatisticaQuestao estatisticaQuestaoCriada = new EstatisticaQuestao(dadosCriacaoEstatisticaQuestaoDTO);
            assunto.setQuestoes(estatisticaQuestaoCriada);
            estatisticaQuestaoCriada.setAssunto(assunto);
            estatisticaQuestaoRepository.save(estatisticaQuestaoCriada);

            return ResponseEntity.ok(new DadosDetalhamentoEstatisticaQuestaoDTO(estatisticaQuestaoCriada));

        }
        return ResponseEntity.badRequest().build();
    }

    public ResponseEntity<Page<DadosDetalhamentoMediaQuestoesDTO>> buscaDadosMediaQuestoes(@PageableDefault(size = 5, page = 0, sort = {"nome"}) Pageable pageable, HttpServletRequest request, Long idProva) {
        Usuario usuario = usuarioService.buscaUsuario(request);
        List<Long> idAssuntosUsuario = retornaListaIdAssuntosFlat(usuario, idProva);
        if (provaPertenceAoUsuario(idProva, request)) {
            return ResponseEntity.ok(estatisticaQuestaoRepository.calcularEstatisticasPorDia(pageable, idAssuntosUsuario));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    public ResponseEntity<Page<DadosListagemEstatisticaQuestoesDTO>> buscaEstatisticaQuestoesPorAssuntoAoLongoDoTempo(Long idMateria, Long idAssunto, HttpServletRequest request, Pageable pageable) {
        if (assuntoPertenceAoUsuario(idAssunto, request) && materiaPertenceAoUsuario(idMateria, request)) {
            return ResponseEntity.ok(estatisticaQuestaoRepository.findAllByAssuntoId(pageable, idAssunto).map(todo.list.api.App.domain.dto.estatistica_questao.DadosListagemEstatisticaQuestoesDTO::new));
        }
        return ResponseEntity.badRequest().build();
    }

    private List<Long> retornaListaIdAssuntosFlat(Usuario usuario, Long idProva) {
        return usuario.getProvas().stream()
                .filter(p -> p.getId().equals(idProva))
                .flatMap(p -> p.getListaDeMaterias().stream()
                        .flatMap(m -> m.getListaAssuntos().stream()
                                .map(Assunto::getId)))
                .collect(Collectors.toList());
    }
    private boolean assuntoPertenceAoUsuario(Long idAssunto, HttpServletRequest request) {
        Assunto assunto = assuntoService.buscarAssuntoEspecificoSemParametrosDePath(idAssunto);
        Usuario usuario = usuarioService.buscaUsuario(request);
        return usuarioService.verificaSeAssuntoPertenceAUsuario(usuario, assunto);
    }
    private boolean materiaPertenceAoUsuario(Long idMateria, HttpServletRequest request) {
        Usuario usuario = usuarioService.buscaUsuario(request);
        Materia materia = materiaService.buscaMateriaEspecifica(idMateria);
        return usuarioService.verificaSeMateriaPertenceAUsuario(usuario, materia);
    }
    private boolean provaPertenceAoUsuario(Long idProva, HttpServletRequest request) {
        Prova prova = provaService.buscaProvaPeloId(idProva);
        return usuarioService.verificaSeProvaPertenceAUsuario(request, prova);
    }

    public ResponseEntity<?> buscaEstatisticaQuestoesPorMateria(Long idMateria, HttpServletRequest request) {
        if (materiaPertenceAoUsuario(idMateria, request)) {
            Optional <MediaQuestoesPorMateriaDTO> mediaPorMateria =  estatisticaQuestaoRepository.buscarMediaQuestoesPorMateria(idMateria);

            if(mediaPorMateria.isPresent()) {
                return ResponseEntity.ok(mediaPorMateria.get());
            }
            Materia materia = materiaService.buscaMateriaEspecifica(idMateria);

            return ResponseEntity.ok(new MediaQuestoesPorMateriaDTO(0L, 0L, 0.0, materia.getId(), materia.getNome()));
        }
        return ResponseEntity.status(400).body("Matéria ou Assunto não pertencem ao usuário");
    }

    public MediaQuestoesPorAssuntoDTO buscaEstatisticaPorAssuntoId(Long idAssunto) {
        Optional<MediaQuestoesPorAssuntoDTO> mediaQuestoesPorAssuntoDTO = estatisticaQuestaoRepository.buscaMediaQuestoesPorAssunto(idAssunto);
        return mediaQuestoesPorAssuntoDTO.orElse(null);
    }

    public ResponseEntity<?> buscaEstatisticaQuestoesPorAssunto(Long idMateria, Long idAssunto, HttpServletRequest request) {
        if (assuntoPertenceAoUsuario(idAssunto, request) && materiaPertenceAoUsuario(idMateria, request)) {
            Optional <MediaQuestoesPorAssuntoDTO> mediaPorAssunto =  estatisticaQuestaoRepository.buscaMediaQuestoesPorAssunto(idAssunto);

            if(mediaPorAssunto.isPresent()) {
                return ResponseEntity.ok(mediaPorAssunto.get());
            }
            Assunto assunto = assuntoService.buscarAssuntoEspecificoSemParametrosDePath(idAssunto);
            return ResponseEntity.ok(new MediaQuestoesPorAssuntoDTO(assunto.getId(),assunto.getNome(), 0L, 0L,  0.0));
        }
        return ResponseEntity.status(400).body("Matéria ou Assunto não pertencem ao usuário");
    }
}
