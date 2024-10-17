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
import todo.list.api.App.domain.dto.estatistica_questao.DadosCriacaoEstatisticaQuestaoDTO;
import todo.list.api.App.domain.dto.estatistica_questao.DadosDetalhamentoEstatisticaQuestaoDTO;
import todo.list.api.App.domain.dto.estatistica_questao.DadosListagemEstatisticaQuestoesDTO;
import todo.list.api.App.domain.services.EstatisticaQuestoesService;

@RestController
@RequestMapping("provas/{idProva}/materias/{idMateria}")
@SecurityRequirement(name =  "bearer-key")
public class EstatisticasQuestoesController {
    @Autowired
    private EstatisticaQuestoesService estatisticaQuestoesService;

    @Transactional
    @PostMapping("/assuntos/{idAssunto}/estatisticas")
    public ResponseEntity<DadosDetalhamentoEstatisticaQuestaoDTO> adicionaEstatisticaQuestoes(@PathVariable Long idMateria, @PathVariable Long idAssunto, HttpServletRequest request, @RequestBody @Valid DadosCriacaoEstatisticaQuestaoDTO dadosCriacaoEstatisticaQuestaoDTO) {
        return estatisticaQuestoesService.adicionarEstatisticaQuestao(idMateria, idAssunto, request, dadosCriacaoEstatisticaQuestaoDTO);
    }
    @GetMapping("/assuntos/{idAssunto}/estatisticas")
    public ResponseEntity<Page<DadosListagemEstatisticaQuestoesDTO>> buscaEstatisticaQuestoes(@PageableDefault(size=5, page=0, sort = {"dataPreenchimento"}) Pageable pageable, @PathVariable Long idMateria, @PathVariable Long idAssunto, HttpServletRequest request) {
        return estatisticaQuestoesService.buscaEstatisticaQuestoesPorAssuntoAoLongoDoTempo(idMateria, idAssunto, request, pageable);
    }
    @GetMapping("/estatisticas/buscaPorMateria")
    public ResponseEntity<?> buscaEstatisticaPorMateria(@PathVariable Long idMateria, HttpServletRequest request) {
        return estatisticaQuestoesService.buscaEstatisticaQuestoesPorMateria(idMateria, request);
    }
    @GetMapping("/assuntos/{idAssunto}/estatisticas/buscaPorAssunto")
    public ResponseEntity<?> buscaEstatisticaPorAssunto(@PathVariable Long idMateria, @PathVariable Long idAssunto , HttpServletRequest request) {
        return estatisticaQuestoesService.buscaEstatisticaQuestoesPorAssunto(idMateria, idAssunto, request);
    }
}
