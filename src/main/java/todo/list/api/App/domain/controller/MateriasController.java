package todo.list.api.App.domain.controller;

import java.util.List;

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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import todo.list.api.App.domain.dto.materia.DadosCriacaoMateriaDTO;
import todo.list.api.App.domain.dto.materia.DadosListagemMateriaDTO;
import todo.list.api.App.domain.dto.questao.DadosCriacaoQuestaoDTO;
import todo.list.api.App.domain.dto.questao.DadosListagemQuestoesDTO;
import todo.list.api.App.domain.model.Materia;
import todo.list.api.App.domain.model.Prova;
import todo.list.api.App.domain.model.Questao;
import todo.list.api.App.domain.model.Usuario;
import todo.list.api.App.domain.repository.MateriaRepository;
import todo.list.api.App.domain.repository.ProvaRepository;
import todo.list.api.App.domain.services.UsuarioService;

@RestController
@RequestMapping("/provas/materias")
public class MateriasController {
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private MateriaRepository materiaRepository;
    @Autowired
    private ProvaRepository provaRepository;

    @GetMapping("/{idProva}")
    public ResponseEntity<Page<DadosListagemMateriaDTO>> getMaterias (
        @PageableDefault(size=10, page=0, sort = {"nome"})Pageable pageable, 
        HttpServletRequest request,
        @PathVariable Long idProva) {

            Usuario usuario = usuarioService.buscaUsuario(request);
            List<Prova> listaDeProvas = usuario.getProvas();
            Prova provaBuscada = provaRepository.getReferenceById(idProva);

            if (listaDeProvas.contains(provaBuscada)) {
                Page<DadosListagemMateriaDTO> listaDeMaterias = materiaRepository.findByProvaId(pageable, idProva)
                .map(DadosListagemMateriaDTO::new);
                return ResponseEntity.ok(listaDeMaterias);
            }
            return null;
            
    }

    @Transactional
    @PostMapping("/{idProva}")
    public ResponseEntity<DadosListagemMateriaDTO> inserirMaterias(
        @RequestBody @Valid DadosCriacaoMateriaDTO dadosMateria, 
        @PathVariable Long idProva,
        HttpServletRequest request) {

        Prova prova = provaRepository.getReferenceById(idProva);

        if (__estaProvaPertenceAEsteUsuario(request, idProva)) {
            Materia materia = new Materia(dadosMateria);
            materia.setProva(prova);
            prova.setListaDeMaterias(materia);

            return ResponseEntity.ok(new DadosListagemMateriaDTO(materia));
        }
        return null;
    }

    @Transactional
    @PostMapping("/{idProva}/questoes/{idMateria}")
    public ResponseEntity<DadosListagemQuestoesDTO> adicionarQuestoes(
        @RequestBody @Valid DadosCriacaoQuestaoDTO dadosQuestao, 
        @PathVariable Long idProva, 
        @PathVariable Long idMateria, 
        HttpServletRequest request) {

        Materia materia = materiaRepository.getReferenceById(idMateria);
        Questao questao = new Questao(dadosQuestao);

        if(__estaProvaPertenceAEsteUsuario(request, idProva)) {
            materia.setQuestoes(questao);
            questao.setMateria(materia);

            return ResponseEntity.ok(new DadosListagemQuestoesDTO(questao));
        }
        return null;
    }

    private boolean __estaProvaPertenceAEsteUsuario(HttpServletRequest request, Long idProva) {
        Usuario usuario = usuarioService.buscaUsuario(request);
        List<Prova> listaDeProvas = usuario.getProvas();
        Prova prova = provaRepository.getReferenceById(idProva);

        return listaDeProvas.contains(prova);
         
    }

}