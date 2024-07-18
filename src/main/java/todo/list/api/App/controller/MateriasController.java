package todo.list.api.App.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import todo.list.api.App.domain.dto.materia.DadosAlteracaoMateriaDTO;
import todo.list.api.App.domain.dto.materia.DadosCriacaoMateriaDTO;
import todo.list.api.App.domain.dto.materia.DadosListagemMateriaDTO;
import todo.list.api.App.domain.services.MateriaService;

@RestController
@RequestMapping("provas/{idProva}/materias")
@SecurityRequirement(name =  "bearer-key")
public class MateriasController {
    @Autowired
    private MateriaService materiaService;

    @GetMapping
    public ResponseEntity<Page<DadosListagemMateriaDTO>> getMaterias (@PageableDefault(size=5, page=0, sort = {"nome"})Pageable pageable, HttpServletRequest request, @PathVariable Long idProva) {
        return materiaService.buscaMaterias(request, idProva, pageable);
    }
    @GetMapping("/{idMateria}")
    public ResponseEntity<DadosListagemMateriaDTO> getMateriaEspecifica (@PageableDefault(size=5, page=0, sort = {"nome"})Pageable pageable, HttpServletRequest request, @PathVariable Long idMateria) {
        return ResponseEntity.ok(new DadosListagemMateriaDTO(materiaService.buscaMateriaEspecifica(idMateria)));
    }
    @Transactional
    @PostMapping
    public ResponseEntity<DadosListagemMateriaDTO> inserirMaterias(@RequestBody @Valid DadosCriacaoMateriaDTO dadosMateria, @PathVariable Long idProva, HttpServletRequest request) {
        return materiaService.inserirMaterias(dadosMateria, idProva, request);
    }

    @Transactional
    @DeleteMapping("/{idMateria}")
    public ResponseEntity<Void> deletarMateria(@PathVariable Long idMateria, HttpServletRequest request) {
        return materiaService.deletarMateria(request, idMateria);
        
    }

    @Transactional
    @PutMapping("/{idMateria}")
    public ResponseEntity<DadosListagemMateriaDTO> alteraMateria (@PathVariable Long idMateria, @RequestBody DadosAlteracaoMateriaDTO dadosAlteracaoMateriaDTO , HttpServletRequest request) {
        return materiaService.alteraMateria(request, dadosAlteracaoMateriaDTO, idMateria);
    }

}