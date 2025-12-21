package todo.list.api.App.domain.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import todo.list.api.App.domain.dto.alternativa.DadosAlteracaoAlternativaDTO;
import todo.list.api.App.domain.dto.alternativa.DadosCriacaoAlternativaDTO;
import todo.list.api.App.domain.dto.alternativa.DadosRespostaQuestaoDTO;
import todo.list.api.App.domain.dto.estatistica_questao.DadosCriacaoEstatisticaQuestaoDTO;
import todo.list.api.App.domain.dto.questao.*;
import todo.list.api.App.domain.model.*;
import todo.list.api.App.domain.repository.AlternativaRepository;
import todo.list.api.App.domain.repository.QuestoesRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class QuestoesService {
    @Autowired
    private QuestoesRepository questaoRepository;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private MateriaService materiaService;
    @Autowired
    private AlternativaRepository alternativaRepository;
    @Autowired
    private PageableService pageableService;
    @Autowired
    private AssuntoService assuntoService;
    @Autowired
    private EstatisticaQuestoesService estatisticaQuestoesService;

    @GetMapping
    public ResponseEntity<?> buscaTodasAsQuestoes(@PathVariable Long idMateria , HttpServletRequest request, @PageableDefault(size=1, page=0, sort = {"textoQuestao"}) Pageable pageable) {
        boolean materiaPertenceAoUsuario = __verificaSeMateriaPertenceAoUsuario(idMateria, request);

        if(materiaPertenceAoUsuario) {
            List<DadosDetalhamentoQuestaoDTO> questoes = questaoRepository.findAllByMateriaId(idMateria)
                    .stream().map(DadosDetalhamentoQuestaoDTO::new).toList();

            if (!questoes.isEmpty()) {
                return ResponseEntity.ok(pageableService.createPageFromList(questoes, pageable));
            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/editar")
    public ResponseEntity<?> buscaTodasAsQuestoesParaEditarOuExcluir(@PathVariable Long idMateria , HttpServletRequest request, @PageableDefault(size=1, page=0, sort = {"textoQuestao"}) Pageable pageable) {
        boolean materiaPertenceAoUsuario = __verificaSeMateriaPertenceAoUsuario(idMateria, request);

        if(materiaPertenceAoUsuario) {
            List<DadosDetalhamentoAlteracaoQuestaoDTO> questoes = questaoRepository.findAllByMateriaId(idMateria)
                    .stream().map(DadosDetalhamentoAlteracaoQuestaoDTO::new).toList();

            if (!questoes.isEmpty()) {
                return ResponseEntity.ok(pageableService.createPageFromList(questoes, pageable));
            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping
    public ResponseEntity<?> criaQuestao(@Valid @RequestBody DadosCriacaoQuestaoDTO dados, @PathVariable Long idMateria, HttpServletRequest request) {
        boolean materiaPertenceAoUsuario = __verificaSeMateriaPertenceAoUsuario(idMateria, request);
        if(materiaPertenceAoUsuario) {
            Materia materia = materiaService.buscaMateriaEspecifica(idMateria);
            Questao questao = new Questao(dados);

            if (dados.idAssunto() != null && !dados.idAssunto().equals(0L)) {
                Assunto assunto = assuntoService.buscarAssuntoEspecificoSemParametrosDePath(dados.idAssunto());
                questao.setAssunto(assunto);
                assunto.addQuestoes(questao);
            }

            List<AlternativaQuestao> alternativasQuestao = dados.listaAlternativas().stream()
                    .map(AlternativaQuestao::new).toList();
            // Setando questao à matéria e vice versa
            questao.setMateria(materia);
            materia.setQuestao(questao);
            // Setando alternativas à questão
            questao.setListaAlternativaQuestao(alternativasQuestao);
            //Setando questão às alternativas
            alternativasQuestao.forEach(a -> a.setQuestao(questao));
            alternativasQuestao.forEach(a -> alternativaRepository.save(a));
            questaoRepository.save(questao);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{idQuestao}")
    public ResponseEntity<?> deletaQuestao(@PathVariable Long idQuestao, @PathVariable Long idMateria, HttpServletRequest request) {
        boolean materiaPertenceAoUsuario = __verificaSeMateriaPertenceAoUsuario(idMateria, request);
        Materia materia = materiaService.buscaMateriaEspecifica(idMateria);
        Questao questaoPertencentenAoUsuario = materia.getListaQuestoes()
                .stream()
                .filter(q -> q.getId().equals(idQuestao))
                .toList().
                get(0);

        if(materiaPertenceAoUsuario && questaoPertencentenAoUsuario != null) {
            materia.removeQuestao(questaoPertencentenAoUsuario);
            if(questaoPertencentenAoUsuario.getAssunto() != null) {
                Assunto assunto = assuntoService.buscarAssuntoEspecificoSemParametrosDePath(questaoPertencentenAoUsuario.getAssunto().getId());
                assunto.removeQuestao(questaoPertencentenAoUsuario);
            }
            questaoRepository.delete(questaoPertencentenAoUsuario);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{idQuestao}")
    public ResponseEntity<?> atualizaQuestaoEAlternativa(@PathVariable Long idQuestao, @PathVariable Long idMateria, HttpServletRequest request, @RequestBody DadosAlteracaoQuestaoDTO dados) {
        boolean materiaPertenceAoUsuario = __verificaSeMateriaPertenceAoUsuario(idMateria, request);
        if (materiaPertenceAoUsuario) {
            Questao questao = questaoRepository.getReferenceById(idQuestao);
            if (dados.textoQuestao() != null) {
                questao.setTextoQuestao(dados.textoQuestao());
            }
            if (dados.idAssunto() != null) {
                Assunto assunto = assuntoService.buscarAssuntoEspecificoSemParametrosDePath(dados.idAssunto());
                questao.setAssunto(assunto);
                assunto.addQuestoes(questao);
            }
            if (dados.listaAlternativas() != null && !dados.listaAlternativas().isEmpty()) {
                List<DadosAlteracaoAlternativaDTO> listaAteracaoAlternativa = dados.listaAlternativas();
                listaAteracaoAlternativa.forEach(a -> {
                    alternativaRepository.getReferenceById(a.id()).setTextoAlternativa(a.textoAlternativa());
                    alternativaRepository.getReferenceById(a.id()).setEhCorreta(a.ehCorreta());
                });
            }
            return ResponseEntity.ok(new DadosDetalhamentoAlteracaoQuestaoDTO(questao));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/{idQuestao}/add-alternativa")
    public ResponseEntity<?> criaAlternativa (@RequestBody DadosCriacaoAlternativaDTO dados, @PathVariable Long idMateria, @PathVariable Long idQuestao, HttpServletRequest request) {
        boolean materiaPertenceAoUsuario = __verificaSeMateriaPertenceAoUsuario(idMateria, request);
        Optional<Questao> questaoBuscada = materiaService.buscaMateriaEspecifica(idMateria).getListaQuestoes().stream().filter(q -> q.getId().equals(idQuestao)).findFirst();
        if(materiaPertenceAoUsuario && questaoBuscada.isPresent()) {
            AlternativaQuestao alternativa = new AlternativaQuestao(dados);
            alternativa.setQuestao(questaoBuscada.get());
            questaoBuscada.get().setAlternativa(alternativa);
            return ResponseEntity.ok(new DadosDetalhamentoQuestaoDTO(questaoBuscada.get()));
        }
        return ResponseEntity.badRequest().build();
    }
    @DeleteMapping("/{idQuestao}/delete-alternativa/{idAlternativa}")
    public ResponseEntity<?> deletaAlternativa(@PathVariable Long idMateria, @PathVariable Long idQuestao, @PathVariable Long idAlternativa, HttpServletRequest request) {
        boolean materiaQuestaoEAlternativaPertencemAoUsuario = __verificaSeQuestaoEAlternativaPertencemAMateriaEAoUsuario(idMateria, idQuestao, idAlternativa, request);
        if (materiaQuestaoEAlternativaPertencemAoUsuario) {
            AlternativaQuestao alternativa = alternativaRepository.getReferenceById(idAlternativa);
            Questao questao = questaoRepository.getReferenceById(idQuestao);
            questao.deletaAlternativa(alternativa);
            alternativaRepository.delete(alternativa);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/{idQuestao}")
    public ResponseEntity<?> verificaSeRespostaEstaCorreta(@PathVariable Long idQuestao, @PathVariable Long idMateria, @RequestBody DadosRespostaQuestaoDTO dados, HttpServletRequest request) {
        boolean materiaPertenceAoUsuario = __verificaSeMateriaPertenceAoUsuario(idMateria, request);

        if(materiaPertenceAoUsuario) {
            AlternativaQuestao alternativa = alternativaRepository.getReferenceById(dados.idAlternativa());
            Questao questao = questaoRepository.getReferenceById(idQuestao);
            if (alternativa.isEhCorreta()) {
                if(questao.getAssunto() != null) {
                    __adicionaEstatisticaDeQuestao(true, idMateria, questao.getAssunto().getId(), request);
                }
                return ResponseEntity.ok(new DadosVerificacaoRespostaCertaDTO("Resposta certa.", true));
            } else {
                if(questao.getAssunto() != null) {
                    __adicionaEstatisticaDeQuestao(false, idMateria, questao.getAssunto().getId(), request);
                }
                return ResponseEntity.ok(new DadosVerificacaoRespostaCertaDTO("Resposta incorreta.", false));
            }
        }
        return ResponseEntity.badRequest().build();
    }

    private void __adicionaEstatisticaDeQuestao(boolean acertou, Long idMateria, Long idAssunto, HttpServletRequest request) {
        LocalDateTime dataAgora = LocalDateTime.now();
        DadosCriacaoEstatisticaQuestaoDTO dadosQuestao;

        if (acertou) {
            dadosQuestao = new DadosCriacaoEstatisticaQuestaoDTO(dataAgora, 1, 1);
        } else {
            dadosQuestao = new DadosCriacaoEstatisticaQuestaoDTO(dataAgora, 0, 1);
        }
        estatisticaQuestoesService.adicionarEstatisticaQuestao(idMateria, idAssunto, request, dadosQuestao);
    }

    private boolean __verificaSeQuestaoEAlternativaPertencemAMateriaEAoUsuario(Long idMateria, Long idQuestao, Long idAlternativa, HttpServletRequest request) {
        Optional <AlternativaQuestao> alternativa = questaoRepository.getReferenceById(idQuestao).getListaAlternativaQuestao().stream().filter(a -> a.getId().equals(idAlternativa)).findFirst();
        return __verificaSeQuestaoPertenceAoUsuario(idMateria, idQuestao, request) && alternativa.isPresent();
    }

    private boolean __verificaSeQuestaoPertenceAoUsuario (Long idMateria, Long idQuestao, HttpServletRequest request) {
        Optional <Questao> questao = materiaService.buscaMateriaEspecifica(idMateria)
                .getListaQuestoes()
                .stream()
                .filter(q -> q.getId().equals(idQuestao))
                .findFirst();

        return __verificaSeMateriaPertenceAoUsuario(idMateria, request) && questao.isPresent();
    }

    private boolean __verificaSeMateriaPertenceAoUsuario(Long idMateria, HttpServletRequest request) {
        Usuario usuario = usuarioService.buscaUsuario(request);
        Materia materia = materiaService.buscaMateriaEspecifica(idMateria);
        return usuarioService.verificaSeMateriaPertenceAUsuario(usuario, materia);
    }
}
