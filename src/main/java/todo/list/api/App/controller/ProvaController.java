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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import todo.list.api.App.domain.dto.prova.DadosCriacaoProvaDTO;
import todo.list.api.App.domain.dto.prova.DadosDetalhamentoProvaDTO;
import todo.list.api.App.domain.dto.prova.DadosListagemProvaDTO;
import todo.list.api.App.domain.services.ProvaService;

@RestController
@RequestMapping("/provas")
@SecurityRequirement(name = "bearer-key")
public class ProvaController {
    @Autowired
    private ProvaService provaService;

    @GetMapping
    public ResponseEntity<Page<DadosListagemProvaDTO>> getProvas(@PageableDefault(size = 5, page = 0, sort = {"dataDaProva"}) Pageable pageable, HttpServletRequest request) {
        return provaService.buscarProvas(pageable, request);
    }
    @GetMapping("/{idProva}")
    public ResponseEntity<DadosDetalhamentoProvaDTO> getProvaEspecifica(@PathVariable Long idProva, HttpServletRequest request) {
        return provaService.buscaProvaEspecifica(idProva, request);
    }
    @Transactional
    @PostMapping
    public ResponseEntity<DadosListagemProvaDTO> inserirProva(@RequestBody @Valid DadosCriacaoProvaDTO dadosProva, HttpServletRequest request) throws Exception {
        return provaService.inserirProva(dadosProva, request);
    }
    @Transactional
    @DeleteMapping("/{idProva}")
    public ResponseEntity<Void> deletarProva(@PathVariable Long idProva, HttpServletRequest request) {
        return provaService.deletarProvaPeloId(request, idProva);
    }
}
