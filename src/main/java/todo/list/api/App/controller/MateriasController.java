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
import todo.list.api.App.domain.dto.materia.DadosCriacaoMateriaDTO;
import todo.list.api.App.domain.dto.materia.DadosListagemMateriaDTO;
import todo.list.api.App.domain.services.MateriaService;

@RestController
@RequestMapping("/materias")
@SecurityRequirement(name =  "bearer-key")
public class MateriasController {
    @Autowired
    private MateriaService materiaService;

    @GetMapping("/{idProva}")
    public ResponseEntity<Page<DadosListagemMateriaDTO>> getMaterias (@PageableDefault(size=5, page=0, sort = {"nome"})Pageable pageable, HttpServletRequest request, @PathVariable Long idProva) {
        return materiaService.buscaMaterias(request, idProva, pageable);
    }

    @Transactional
    @PostMapping("/{idProva}")
    public ResponseEntity<DadosListagemMateriaDTO> inserirMaterias(@RequestBody @Valid DadosCriacaoMateriaDTO dadosMateria, @PathVariable Long idProva, HttpServletRequest request) {
        return materiaService.inserirMaterias(dadosMateria, idProva, request);
    }

}