package todo.list.api.App.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import todo.list.api.App.domain.dto.alternativa.DadosCriacaoAlternativaDTO;
import todo.list.api.App.domain.dto.alternativa.DadosRespostaQuestaoDTO;
import todo.list.api.App.domain.dto.questao.*;
import todo.list.api.App.domain.services.*;

@RestController
@RequestMapping("/provas/{idProva}/materias/{idMateria}/questoes")
public class QuestoesController {
    @Autowired
    private QuestoesService questoesService;

    @GetMapping
    public ResponseEntity<?> buscaTodasAsQuestoes(@PathVariable Long idMateria , HttpServletRequest request, @PageableDefault(size=1, page=0, sort = {"textoQuestao"}) Pageable pageable) {
        return questoesService.buscaTodasAsQuestoes(idMateria, request, pageable);
    }

    @GetMapping("/editar")
    public ResponseEntity<?> buscaTodasAsQuestoesParaEditarOuExcluir(@PathVariable Long idMateria , HttpServletRequest request, @PageableDefault(size=1, page=0, sort = {"textoQuestao"}) Pageable pageable) {
        return questoesService.buscaTodasAsQuestoesParaEditarOuExcluir(idMateria, request, pageable);
    }

    @Transactional
    @PostMapping
    public ResponseEntity<?> criaQuestao(@Valid @RequestBody DadosCriacaoQuestaoDTO dados, @PathVariable Long idMateria, HttpServletRequest request) {
        return questoesService.criaQuestao(dados, idMateria, request);
    }

    @Transactional
    @DeleteMapping("/{idQuestao}")
    public ResponseEntity<?> deletaQuestao(@PathVariable Long idQuestao, @PathVariable Long idMateria, HttpServletRequest request) {
        return questoesService.deletaQuestao(idQuestao, idMateria, request);
    }

    @Transactional
    @PutMapping("/{idQuestao}")
    public ResponseEntity<?> atualizaQuestaoEAlternativa(@PathVariable Long idQuestao, @PathVariable Long idMateria, HttpServletRequest request, @RequestBody DadosAlteracaoQuestaoDTO dados) {
        return questoesService.atualizaQuestaoEAlternativa(idQuestao, idMateria, request, dados);
    }

    @Transactional
    @PostMapping("/{idQuestao}/add-alternativa")
    public ResponseEntity<?> criaAlternativa (@RequestBody DadosCriacaoAlternativaDTO dados, @PathVariable Long idMateria, @PathVariable Long idQuestao, HttpServletRequest request) {
        return questoesService.criaAlternativa(dados, idMateria, idQuestao, request);
    }
    @Transactional
    @DeleteMapping("/{idQuestao}/delete-alternativa/{idAlternativa}")
    public ResponseEntity<?> deletaAlternativa(@PathVariable Long idMateria, @PathVariable Long idQuestao, @PathVariable Long idAlternativa, HttpServletRequest request) {
        return questoesService.deletaAlternativa(idMateria, idQuestao, idAlternativa, request);
    }

    @PostMapping("/{idQuestao}")
    public ResponseEntity<?> verificaSeRespostaEstaCorreta(@PathVariable Long idQuestao, @PathVariable Long idMateria, @RequestBody DadosRespostaQuestaoDTO dados, HttpServletRequest request) {
        return questoesService.verificaSeRespostaEstaCorreta(idQuestao, idMateria,dados, request);
    }

}
