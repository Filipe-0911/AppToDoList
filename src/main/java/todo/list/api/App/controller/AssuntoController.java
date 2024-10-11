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
import todo.list.api.App.domain.dto.assunto.*;
import todo.list.api.App.domain.services.AssuntoService;

@RestController
@RequestMapping
@SecurityRequirement(name="bearer-key")
public class AssuntoController {
    @Autowired
    private AssuntoService assuntoService;

    @GetMapping("provas/{idProva}/materias/{idMateria}/assuntos")
    public ResponseEntity<Page<DadosListagemAssuntoDTO>> buscaAssuntos(@PageableDefault(size=5, page=0, sort = {"nome"})Pageable pageable, HttpServletRequest request, @PathVariable Long idMateria) {
        return assuntoService.buscaAssuntos(pageable, request, idMateria);
    }
    @Transactional
    @PostMapping("provas/{idProva}/materias/{idMateria}/assuntos")
    public ResponseEntity<DadosDetalhamentoAssuntoDTO> criarAssunto(@RequestBody @Valid DadosCriacaoAsssuntoDTO dadosCriacaoAsssuntoDTO, HttpServletRequest request, @PathVariable Long idMateria) {
        return assuntoService.criarAssunto(dadosCriacaoAsssuntoDTO, request, idMateria);
    }
    @GetMapping("provas/{idProva}/materias/{idMateria}/assuntos/{idAssunto}")
    public ResponseEntity<DadosDetalhamentoAssuntoDTO> buscaAssuntoEspecifico(@PathVariable Long idMateria, @PathVariable Long idAssunto, HttpServletRequest request) {
        return assuntoService.buscaAssuntoEspecifico(idMateria, idAssunto, request);
    }
    @Transactional
    @DeleteMapping("provas/{idProva}/materias/{idMateria}/assuntos/{idAssunto}")
    public ResponseEntity<Void> deletarAssunto(@PathVariable Long idMateria, @PathVariable Long idAssunto, HttpServletRequest request) {
        return assuntoService.deletarAssunto(idMateria, idAssunto, request);
    }
    @Transactional
    @PutMapping("provas/{idProva}/materias/{idMateria}/assuntos/{idAssunto}")
    public ResponseEntity<DadosDetalhamentoAssuntoDTO> atualizaAssunto(@PathVariable Long idMateria, @PathVariable Long idAssunto, @RequestBody @Valid DadosAlteracaoAssuntoDTO dadosAlteracaoAssuntoDTO, HttpServletRequest request) {
        return assuntoService.atualizaAssunto(idMateria, idAssunto, request, dadosAlteracaoAssuntoDTO);
    }

    @Transactional
    @PostMapping("provas/{idProva}/materias/{idMateria}/assuntos/{idAssunto}/comentarios")
    public ResponseEntity<DadosDetalhamentoAssuntoDTO> insereComentariosAoAssunto(@PathVariable Long idMateria, @PathVariable Long idAssunto, @RequestBody @Valid DadosComentariosAssuntoDTO dadosComentariosAssuntoDTO, HttpServletRequest request) {
        return assuntoService.insereComentarios(idMateria, idAssunto, request, dadosComentariosAssuntoDTO);
    }

    @Transactional
    @PutMapping("provas/{idProva}/materias/{idMateria}/assuntos/{idAssunto}/comentarios")
    public ResponseEntity<DadosComentariosAssuntoDTO> alteraComentarios(@PathVariable Long idMateria, @PathVariable Long idAssunto, @RequestBody @Valid DadosComentariosAssuntoDTO dadosComentariosAssuntoDTO, HttpServletRequest request) {
        return assuntoService.alteraComentarios(idMateria, idAssunto, request, dadosComentariosAssuntoDTO);
    }

    @GetMapping("assuntos/{nomeAssunto}")
    public ResponseEntity<?> buscaAssuntoPorNome (@PathVariable String nomeAssunto, HttpServletRequest request) {
        return assuntoService.buscaAssuntoPorNome(nomeAssunto, request);
    }

    @GetMapping("assuntos?idMateria={idMateria}")
    public ResponseEntity<?> buscarAssuntoPorIdMateria(@PathVariable Long idMateria, HttpServletRequest request, @PageableDefault(size=5, page=0, sort = {"nome"})Pageable pageable) {
        return assuntoService.buscaAssuntosPorIdMateria(idMateria, request, pageable);
    }

    @GetMapping("assuntos")
    public ResponseEntity<?> buscarTodosOsAssuntos(HttpServletRequest request, @PageableDefault(size=100, page=0, sort = {"nome"})Pageable pageable) {
        return assuntoService.buscarTodosOsAssuntos(request, pageable);
    }

}
