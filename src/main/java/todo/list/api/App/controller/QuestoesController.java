package todo.list.api.App.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import todo.list.api.App.domain.dto.questao.DadosCriacaoQuestaoDTO;
import todo.list.api.App.domain.dto.questao.DadosDetalhamentoQuestaoDTO;
import todo.list.api.App.domain.dto.questao.DadosListagemQuestoesDTO;
import todo.list.api.App.domain.services.QuestaoService;

@RestController
@RequestMapping("provas/{idProva}/materias/{idMateria}/assuntos/{idAssunto}/questoes")
@SecurityRequirement(name =  "bearer-key")
public class QuestoesController {
    @Autowired
    private QuestaoService questaoService;

    @Transactional
    @PostMapping
    public ResponseEntity<DadosDetalhamentoQuestaoDTO> criarQuestoes(@PathVariable Long idMateria, @PathVariable Long idAssunto, HttpServletRequest request, @RequestBody @Valid DadosCriacaoQuestaoDTO dadosCriacaoQuestaoDTO) {
        return questaoService.criarQuestao(idMateria, idAssunto, request, dadosCriacaoQuestaoDTO);
    }
    @GetMapping
    public ResponseEntity<Page<DadosListagemQuestoesDTO>> buscaQuestoes(@PageableDefault(size=5, page=0, sort = {"dataPreenchimento"}) Pageable pageable, @PathVariable Long idMateria, @PathVariable Long idAssunto, HttpServletRequest request) {
        return questaoService.buscaQuestoesDoAssunto(idMateria, idAssunto, request, pageable);
    }
}
