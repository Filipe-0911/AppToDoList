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

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import todo.list.api.App.domain.dto.assunto.DadosAlteracaoAssuntoDTO;
import todo.list.api.App.domain.dto.assunto.DadosCriacaoAsssuntoDTO;
import todo.list.api.App.domain.dto.assunto.DadosDetalhamentoAssuntoDTO;
import todo.list.api.App.domain.dto.assunto.DadosListagemAssuntoDTO;
import todo.list.api.App.domain.services.AssuntoService;

@RestController
@RequestMapping("provas/{idProva}/materias/{idMateria}/assuntos")
@SecurityRequirement(name="bearer-key")
public class AssuntoController {
    @Autowired
    private AssuntoService assuntoService;

    @GetMapping
    public ResponseEntity<Page<DadosListagemAssuntoDTO>> buscaAssuntos(@PageableDefault(size=5, page=0, sort = {"nome"})Pageable pageable, HttpServletRequest request, @PathVariable Long idMateria) {
        return assuntoService.buscaAssuntos(pageable, request, idMateria);
    }
    @Transactional
    @PostMapping
    public ResponseEntity<DadosListagemAssuntoDTO> criarAssunto(@RequestBody @Valid DadosCriacaoAsssuntoDTO dadosCriacaoAsssuntoDTO, HttpServletRequest request, @PathVariable Long idMateria) {
        return assuntoService.criarAssunto(dadosCriacaoAsssuntoDTO, request, idMateria);
    }
    @GetMapping("/{idAssunto}")
    public ResponseEntity<DadosDetalhamentoAssuntoDTO> buscaAssuntoEspecifico(@PathVariable Long idMateria, @PathVariable Long idAssunto, HttpServletRequest request) {
        return assuntoService.buscaAssuntoEspecifico(idMateria, idAssunto, request);
    }
    @Transactional
    @DeleteMapping("/{idAssunto}")
    public ResponseEntity<Void> deletarAssunto(@PathVariable Long idMateria, @PathVariable Long idAssunto, HttpServletRequest request) {
        return assuntoService.deletarAssunto(idMateria, idAssunto, request);
    }
    @Transactional
    @PutMapping("/{idAssunto}")
    public ResponseEntity<DadosDetalhamentoAssuntoDTO> atualizaAssunto(@PathVariable Long idMateria, @PathVariable Long idAssunto, @RequestBody @Valid DadosAlteracaoAssuntoDTO dadosAlteracaoAssuntoDTO, HttpServletRequest request) {
        return assuntoService.atualizaAssunto(idMateria, idAssunto, request, dadosAlteracaoAssuntoDTO);
    }

}
