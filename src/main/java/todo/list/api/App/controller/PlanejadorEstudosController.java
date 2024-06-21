package todo.list.api.App.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import todo.list.api.App.domain.dto.planejadorestudos.DadosAlteracaoPlanejadorEstudosDTO;
import todo.list.api.App.domain.dto.planejadorestudos.DadosCriacaoPlanejadorEstudosDTO;
import todo.list.api.App.domain.dto.planejadorestudos.DadosDetalhamentoPlanejadorEstudosDTO;
import todo.list.api.App.domain.dto.planejadorestudos.DadosListagemPlanejadorEstudosDTO;
import todo.list.api.App.domain.services.PlanejadorEstudosService;

@RestController
@RequestMapping("/planejador")
public class PlanejadorEstudosController {
    @Autowired
    private PlanejadorEstudosService planejadorEstudosService;

    @Transactional
    @PostMapping("/{idAssunto}")
    public ResponseEntity<DadosListagemPlanejadorEstudosDTO> inserePlanejadorEstudos(@PathVariable Long idAssunto, @RequestBody @Valid DadosCriacaoPlanejadorEstudosDTO dadosCriacaoPlanejadorEstudosDTO, HttpServletRequest request) {
        return planejadorEstudosService.inserePlanejadorEstudos(idAssunto, dadosCriacaoPlanejadorEstudosDTO, request);
    }

    @GetMapping("/{idAssunto}")
    public ResponseEntity<Page<DadosListagemPlanejadorEstudosDTO>> listaPlanejador(@PageableDefault(size = 5, page = 0, sort = {"dataInicio"}) Pageable pageable, @PathVariable Long idAssunto, HttpServletRequest request) {
        return planejadorEstudosService.listaPlanejador(pageable, idAssunto, request);
    }

    @GetMapping("/especifico/{idPlanejador}")
    public ResponseEntity<DadosDetalhamentoPlanejadorEstudosDTO> buscaPlanejadorEspecifico(@PathVariable Long idPlanejador, HttpServletRequest request) {
        return planejadorEstudosService.buscaPlanejadorEspecifico(idPlanejador, request);
    }

    @Transactional
    @DeleteMapping("/especifico/{idPlanejador}")
    public ResponseEntity<Void> deletaPlanejamento(@PathVariable Long idPlanejador, HttpServletRequest request) {
        return planejadorEstudosService.deletaPlanejamento(idPlanejador, request);
    }

    @Transactional
    @PutMapping("/especifico/{idPlanejador}")
    public ResponseEntity<DadosDetalhamentoPlanejadorEstudosDTO> alteraPlanejamento(@PathVariable Long idPlanejador, HttpServletRequest request, @RequestBody DadosAlteracaoPlanejadorEstudosDTO dadosAlteracaoPlanejadorEstudosDTO) {
        return planejadorEstudosService.alteraPlanejamento(idPlanejador, request, dadosAlteracaoPlanejadorEstudosDTO);
    }

    @Transactional
    @PutMapping("/especifico/cancelar/{idPlanejador}")
    public ResponseEntity<DadosDetalhamentoPlanejadorEstudosDTO> cancelaPlanejamentoEstudo (@PathVariable Long idPlanejador, HttpServletRequest request) {
        return planejadorEstudosService.cancelarPlanejamentoEstudos(idPlanejador, request);
    }


}
