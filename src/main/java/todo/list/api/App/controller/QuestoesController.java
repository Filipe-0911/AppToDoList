package todo.list.api.App.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import todo.list.api.App.domain.dto.questao.DadosCriacaoQuestaoDTO;
import todo.list.api.App.domain.dto.questao.DadosDetalhamentoQuestaoDTO;
import todo.list.api.App.domain.dto.questao.DadosListagemQuestoesDTO;
import todo.list.api.App.domain.model.Usuario;
import todo.list.api.App.domain.services.AssuntoService;
import todo.list.api.App.domain.services.QuestaoService;
import todo.list.api.App.domain.services.UsuarioService;

@RestController
@RequestMapping("provas/{idProva}/materias/{idMateria}/assuntos/{idAssunto}/questoes")
public class QuestoesController {
    @Autowired
    private AssuntoService assuntoService;
    @Autowired
    private UsuarioService usuarioService;
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
