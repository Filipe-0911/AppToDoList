package todo.list.api.App.domain.services;

import java.util.List;
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
import todo.list.api.App.domain.dto.questao.DadosCriacaoQuestaoDTO;
import todo.list.api.App.domain.dto.questao.DadosDetalhamentoQuestaoDTO;
import todo.list.api.App.domain.dto.questao.DadosListagemQuestoesDTO;
import todo.list.api.App.domain.model.Assunto;
import todo.list.api.App.domain.model.Materia;
import todo.list.api.App.domain.model.Prova;
import todo.list.api.App.domain.model.Questao;
import todo.list.api.App.domain.model.Usuario;
import todo.list.api.App.domain.repository.QuestaoRepository;

@Service
public class QuestaoService {
    @Autowired
    private QuestaoRepository questaoRepository;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ProvaService provaService;
    @Autowired
    private MateriaService materiaService;
    @Autowired
    private AssuntoService assuntoService;

    public ResponseEntity<DadosDetalhamentoQuestaoDTO> criarQuestao(Long idMateria, Long idAssunto,HttpServletRequest request, DadosCriacaoQuestaoDTO dadosCriacaoQuestaoDTO) {
        Assunto assunto = assuntoService.buscarAssuntoEspecificoSemParametrosDePath(idAssunto);

        if (materiaPertenceAoUsuario(idMateria, request)) {
            Questao questaoBuscada = questaoRepository.findByDataPreenchimentoAndAssuntoId(dadosCriacaoQuestaoDTO.dataPreenchimento(), assunto.getId());
            if (questaoBuscada != null && assunto.getId().equals(questaoBuscada.getAssunto().getId())) {
                questaoBuscada.setQuestoesAcertadas(dadosCriacaoQuestaoDTO.questoesAcertadas());
                questaoBuscada.setQuestoesFeitas(dadosCriacaoQuestaoDTO.questoesFeitas());
                return ResponseEntity.ok(new DadosDetalhamentoQuestaoDTO(questaoBuscada));
            }
            Questao questaoCriada = new Questao(dadosCriacaoQuestaoDTO);
            assunto.setQuestoes(questaoCriada);
            questaoCriada.setAssunto(assunto);
            questaoRepository.save(questaoCriada);

            return ResponseEntity.ok(new DadosDetalhamentoQuestaoDTO(questaoCriada));

        }
        return ResponseEntity.badRequest().build();
    }

    public ResponseEntity<Page<DadosDetalhamentoMediaQuestoesDTO>> buscaDadosMediaQuestoes(@PageableDefault(size = 5, page = 0, sort = {"nome"}) Pageable pageable, HttpServletRequest request, Long idProva) {
        Usuario usuario = usuarioService.buscaUsuario(request);
        List<Long> idAssuntosUsuario = retornaListaIdAssuntosFlat(usuario, idProva);
        if (provaPertenceAoUsuario(idProva, request)) {
            return ResponseEntity.ok(questaoRepository.calcularEstatisticasPorDia(pageable, idAssuntosUsuario));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    public ResponseEntity<Page<DadosListagemQuestoesDTO>> buscaQuestoesDoAssunto(Long idMateria, Long idAssunto, HttpServletRequest request, Pageable pageable) {
        if (assuntoPertenceAoUsuario(idAssunto, request) && materiaPertenceAoUsuario(idMateria, request)) {
            return ResponseEntity.ok(questaoRepository.findAllByAssuntoId(pageable, idAssunto).map(DadosListagemQuestoesDTO::new));
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
}
