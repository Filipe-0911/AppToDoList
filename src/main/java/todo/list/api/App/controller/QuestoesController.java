package todo.list.api.App.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import todo.list.api.App.domain.dto.alternativa.DadosRespostaQuestaoDTO;
import todo.list.api.App.domain.dto.questao.DadosAlteracaoQuestaoDTO;
import todo.list.api.App.domain.dto.questao.DadosCriacaoQuestaoDTO;
import todo.list.api.App.domain.dto.questao.DadosDetalhamentoQuestaoDTO;
import todo.list.api.App.domain.dto.questao.DadosVerificacaoRespostaCertaDTO;
import todo.list.api.App.domain.model.AlternativaQuestao;
import todo.list.api.App.domain.model.Materia;
import todo.list.api.App.domain.model.Questao;
import todo.list.api.App.domain.model.Usuario;
import todo.list.api.App.domain.repository.AlternativaRepository;
import todo.list.api.App.domain.repository.QuestoesRepository;
import todo.list.api.App.domain.services.MateriaService;
import todo.list.api.App.domain.services.PageableService;
import todo.list.api.App.domain.services.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/provas/{idProva}/materias/{idMateria}/questoes")
public class QuestoesController {
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

    @Transactional
    @PostMapping
    public ResponseEntity<?> criarQuestao(@Valid @RequestBody DadosCriacaoQuestaoDTO dados, @PathVariable Long idMateria, HttpServletRequest request) {
        boolean materiaPertenceAoUsuario = __verificaSeMateriaPertenceAoUsuario(idMateria, request);
        if(materiaPertenceAoUsuario) {
            Materia materia = materiaService.buscaMateriaEspecifica(idMateria);
            Questao questao = new Questao(dados);

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

    @Transactional
    @DeleteMapping("/{idQuestao}")
    public ResponseEntity<?> deletarQuestao(@PathVariable Long idQuestao, @PathVariable Long idMateria, HttpServletRequest request) {
        boolean materiaPertenceAoUsuario = __verificaSeMateriaPertenceAoUsuario(idMateria, request);
        if(materiaPertenceAoUsuario) {
            Materia materia = materiaService.buscaMateriaEspecifica(idMateria);
            Questao questao = questaoRepository.getReferenceById(idQuestao);
            materia.removeQuestao(questao);
            questaoRepository.delete(questao);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/{idQuestao}")
    public ResponseEntity<?> verificaSeRespostaEstaCorreta(@PathVariable Long idQuestao, @PathVariable Long idMateria, @RequestBody DadosRespostaQuestaoDTO dados, HttpServletRequest request) {
        boolean materiaPertenceAoUsuario = __verificaSeMateriaPertenceAoUsuario(idMateria, request);

        if(materiaPertenceAoUsuario) {
            Questao questao = questaoRepository.getReferenceById(idQuestao);
            String respostaCerta = questao.getTextoRespostaCerta();

            if (respostaCerta.equalsIgnoreCase(dados.respostaEscolhida())) {
                return ResponseEntity.ok(new DadosVerificacaoRespostaCertaDTO("Resposta certa.", true));
            } else {
                return ResponseEntity.ok(new DadosVerificacaoRespostaCertaDTO("Resposta incorreta.", false));
            }
        }
        return ResponseEntity.badRequest().build();
    }

    private void __adicionaEstatisticaDeQuestao(boolean acertou, Long idQuestao) {
        // criar lógica de adição de estatística
    }

    private boolean __verificaSeMateriaPertenceAoUsuario(Long idMateria, HttpServletRequest request) {
        Usuario usuario = usuarioService.buscaUsuario(request);
        Materia materia = materiaService.buscaMateriaEspecifica(idMateria);
        return usuarioService.verificaSeMateriaPertenceAUsuario(usuario, materia);
    }

}
