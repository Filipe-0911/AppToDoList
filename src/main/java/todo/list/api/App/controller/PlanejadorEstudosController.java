package todo.list.api.App.controller;

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
import todo.list.api.App.domain.dto.planejadorestudos.DadosCriacaoPlanejadorEstudosDTO;
import todo.list.api.App.domain.dto.planejadorestudos.DadosListagemPlanejadorEstudosDTO;
import todo.list.api.App.domain.model.Assunto;
import todo.list.api.App.domain.model.Materia;
import todo.list.api.App.domain.model.PlanejadorEstudos;
import todo.list.api.App.domain.model.Usuario;
import todo.list.api.App.domain.repository.PlanejadorEstudosRepository;
import todo.list.api.App.domain.services.AssuntoService;
import todo.list.api.App.domain.services.UsuarioService;

@RestController
@RequestMapping("/planejador")
public class PlanejadorEstudosController {

    @Autowired
    private PlanejadorEstudosRepository planejadorEstudosRepository;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private AssuntoService assuntoService;

    @Transactional
    @PostMapping("/{idAssunto}")
    public ResponseEntity<DadosListagemPlanejadorEstudosDTO> inserePlanejadorEstudos(@PathVariable Long idAssunto, @RequestBody @Valid DadosCriacaoPlanejadorEstudosDTO dadosCriacaoPlanejadorEstudosDTO, HttpServletRequest request) {
        Usuario usuario = usuarioService.buscaUsuario(request);
        Assunto assunto = assuntoService.buscarAssuntoEspecificoSemParametrosDePath(idAssunto);
        Materia materia = assunto.getMateria();
        if (usuarioService.verificaSeMateriaPertenceAUsuario(usuario, materia)) {
            PlanejadorEstudos planejadorEstudos = new PlanejadorEstudos(null,
                    dadosCriacaoPlanejadorEstudosDTO.dataInicio(),
                    assunto,
                    dadosCriacaoPlanejadorEstudosDTO.dataTermino(),
                    false,
                    usuario);

            planejadorEstudosRepository.save(planejadorEstudos);
            usuario.setPlanejadorEstudos(planejadorEstudos);
            assunto.setPlanejadorEstudos(planejadorEstudos);

            return ResponseEntity.ok(new DadosListagemPlanejadorEstudosDTO(planejadorEstudos));
            
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{idAssunto}")
    public ResponseEntity<Page<DadosListagemPlanejadorEstudosDTO>> listaPlanejador(@PageableDefault(size=5, page=0, sort = {"dataInicio"}) Pageable pageable, @PathVariable Long idAssunto, HttpServletRequest request) {
        Usuario usuario = usuarioService.buscaUsuario(request);
        Assunto assunto = assuntoService.buscarAssuntoEspecificoSemParametrosDePath(idAssunto);
        Materia materia = assunto.getMateria();
        if(usuarioService.verificaSeMateriaPertenceAUsuario(usuario, materia)) {
            Page<DadosListagemPlanejadorEstudosDTO> listaDePlanejadorEstudos = planejadorEstudosRepository.findAllByAssuntoId(pageable, idAssunto)
            .map(p -> new DadosListagemPlanejadorEstudosDTO(p));
            return ResponseEntity.ok(listaDePlanejadorEstudos);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/especifico/{idPlanejador}")
    public ResponseEntity<DadosListagemPlanejadorEstudosDTO> buscaPlanejadorEspecifico(@PathVariable Long idPlanejador, HttpServletRequest request) {
        Usuario usuario = usuarioService.buscaUsuario(request);
        PlanejadorEstudos planejadorEstudos = planejadorEstudosRepository.getReferenceById(idPlanejador);
        List<PlanejadorEstudos> listaPlanejadorEstudosUsuario = usuario.getPlanejadorEstudos();
        boolean planejadorPertenceAUsuario = listaPlanejadorEstudosUsuario.contains(planejadorEstudos);
        if(planejadorPertenceAUsuario) {

            return ResponseEntity.ok(new DadosListagemPlanejadorEstudosDTO(planejadorEstudos));
        }
        
        return ResponseEntity.badRequest().build();
    }

}
