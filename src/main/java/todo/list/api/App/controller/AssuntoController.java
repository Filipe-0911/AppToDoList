package todo.list.api.App.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import todo.list.api.App.domain.dto.assunto.DadosCriacaoAsssuntoDTO;
import todo.list.api.App.domain.dto.assunto.DadosDetalhamentoAssuntoDTO;
import todo.list.api.App.domain.dto.assunto.DadosListagemAssuntoDTO;
import todo.list.api.App.domain.dto.questao.DadosCriacaoQuestaoDTO;
import todo.list.api.App.domain.dto.questao.DadosListagemQuestoesDTO;
import todo.list.api.App.domain.model.Assunto;
import todo.list.api.App.domain.model.Materia;
import todo.list.api.App.domain.model.Questao;
import todo.list.api.App.domain.model.Usuario;
import todo.list.api.App.domain.repository.AssuntoRepository;
import todo.list.api.App.domain.repository.MateriaRepository;
import todo.list.api.App.domain.repository.QuestaoRepository;
import todo.list.api.App.domain.services.UsuarioService;

@RestController
@RequestMapping("/assuntos")
@SecurityRequirement(name="bearer-key")
public class AssuntoController {
    @Autowired
    private AssuntoRepository assuntoRepository;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private MateriaRepository materiaRepository;
    @Autowired
    private QuestaoRepository questaoRepository;

    @GetMapping("/{idMateria}")
    public ResponseEntity<Page<DadosListagemAssuntoDTO>> buscaAssuntos(@PageableDefault(size=5, page=0, sort = {"nome"})Pageable pageable, HttpServletRequest request, @PathVariable Long idMateria) {
        boolean materiaPertenceAUsuario = __verificaSeMateriaPertenceAUsuario(request, idMateria);

        if (materiaPertenceAUsuario) {
            Page<DadosListagemAssuntoDTO> listaAssuntosDTOs = assuntoRepository.findByMateriaId(pageable, idMateria)
                .map(DadosListagemAssuntoDTO::new);

                return ResponseEntity.ok(listaAssuntosDTOs);
        }
        return null;
    }

    @PostMapping("/{idMateria}")
    public ResponseEntity<DadosListagemAssuntoDTO> criarAssunto(@RequestBody @Valid DadosCriacaoAsssuntoDTO dadosCriacaoAsssuntoDTO, HttpServletRequest request, @PathVariable Long idMateria) {
        boolean materiaPertenceAUsuario = __verificaSeMateriaPertenceAUsuario(request, idMateria);

        if(materiaPertenceAUsuario) {
            Assunto assunto = new Assunto(dadosCriacaoAsssuntoDTO);
            Materia materia = materiaRepository.getReferenceById(idMateria);
            materia.setAssunto(assunto);
            assunto.setMateria(materia);
            assuntoRepository.save(assunto);
            return ResponseEntity.ok(new DadosListagemAssuntoDTO(assunto));
        }
        return null;
    }

    @GetMapping("{idMateria}/{idAssunto}")
    public ResponseEntity<DadosDetalhamentoAssuntoDTO> buscaAssuntoEspecifico(@PathVariable Long idMateria, @PathVariable Long idAssunto, HttpServletRequest request) {
        boolean materiaPertenceAUsuario = __verificaSeMateriaPertenceAUsuario(request, idMateria);
        if(materiaPertenceAUsuario) {
            Assunto assunto = assuntoRepository.getReferenceById(idAssunto);
            return ResponseEntity.ok(new DadosDetalhamentoAssuntoDTO(assunto));
        }
        return null;

    }

    @PostMapping("{idMateria}/{idAssunto}")
    public ResponseEntity<Page<DadosListagemQuestoesDTO>> buscarQuestoes(
        @PathVariable Long idMateria,
        @PathVariable Long idAssunto,
        HttpServletRequest request,
        @RequestBody @Valid DadosCriacaoQuestaoDTO dadosCriacaoQuestaoDTO
    ) {
        boolean materiaPertenceAUsuario = __verificaSeMateriaPertenceAUsuario(request, idMateria);
        if(materiaPertenceAUsuario) {
            Questao questao = new Questao(dadosCriacaoQuestaoDTO);
            Assunto assunto = assuntoRepository.getReferenceById(idAssunto);
            assunto.setQuestoes(questao);
            questao.setAssunto(assunto);
            questaoRepository.save(questao);
        }
        return null;
    }

    private boolean __verificaSeMateriaPertenceAUsuario(HttpServletRequest request, Long idMateria) {
        Usuario usuario = usuarioService.buscaUsuario(request);
        Materia materia = materiaRepository.getReferenceById(idMateria);

        return usuarioService.verificaSeMateriaPertenceAUsuario(usuario, materia);
    }

}
