package todo.list.api.App.domain.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import todo.list.api.App.domain.dto.prova.DadosCriacaoProvaDTO;
import todo.list.api.App.domain.dto.prova.DadosListagemProvaDTO;
import todo.list.api.App.domain.model.Prova;
import todo.list.api.App.domain.model.Usuario;
import todo.list.api.App.domain.repository.ProvaRepository;
import todo.list.api.App.domain.services.UsuarioService;

@RestController
@RequestMapping("/provas")
public class ProvaController {
    @Autowired
    private ProvaRepository provaRepository;
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<Page<DadosListagemProvaDTO>> getProvas(@PageableDefault(size=10, page=0, sort = {"dataDaProva"})Pageable pageable, HttpServletRequest request) {
        Long id = usuarioService.buscaUsuario(request).getId();
        if (id != null) {
            Page<DadosListagemProvaDTO> listaDeProvas = provaRepository.findAllByUsuarioId(pageable, id)
                .map(DadosListagemProvaDTO::new);

            return ResponseEntity.ok(listaDeProvas);
        }
        return null;
    }

    @Transactional
    @PostMapping
    public ResponseEntity<DadosListagemProvaDTO> inserirProva(@RequestBody @Valid DadosCriacaoProvaDTO dadosProva, HttpServletRequest request) {
        Usuario usuario = usuarioService.buscaUsuario(request);
        Prova prova = new Prova(dadosProva);
        prova.setUsuario(usuario);

        usuario.setProvas(prova);

        DadosListagemProvaDTO provaDto = new DadosListagemProvaDTO(prova);
        return ResponseEntity.ok(provaDto);
    }

}
