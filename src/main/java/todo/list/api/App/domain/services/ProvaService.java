package todo.list.api.App.domain.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import todo.list.api.App.domain.dto.prova.DadosCriacaoProvaDTO;
import todo.list.api.App.domain.dto.prova.DadosDetalhamentoProvaDTO;
import todo.list.api.App.domain.dto.prova.DadosListagemProvaDTO;
import todo.list.api.App.domain.model.Prova;
import todo.list.api.App.domain.model.Usuario;
import todo.list.api.App.domain.repository.ProvaRepository;

@Service
public class ProvaService {
    @Autowired
    private ProvaRepository provaRepository;
    @Autowired
    private UsuarioService usuarioService;

    public ResponseEntity<Page<DadosDetalhamentoProvaDTO>> buscarProvas(@PageableDefault(size=10, page=0, sort = {"dataDaProva"})Pageable pageable, HttpServletRequest request) {
        Long id = usuarioService.buscaUsuario(request).getId();
        if (id != null) {
            Page<DadosDetalhamentoProvaDTO> listaDeProvas = provaRepository.findAllByUsuarioId(pageable, id)
                .map(DadosDetalhamentoProvaDTO::new);

            return ResponseEntity.ok(listaDeProvas);
        }
        return null;
    }
    
    public ResponseEntity<DadosListagemProvaDTO> inserirProva(@Valid DadosCriacaoProvaDTO dadosProva, HttpServletRequest request) {
                
        Usuario usuario = usuarioService.buscaUsuario(request);
        Prova prova = new Prova(dadosProva);

        prova.setUsuario(usuario);
        usuario.setProvas(prova);

        DadosListagemProvaDTO provaDto = new DadosListagemProvaDTO(prova);
        return ResponseEntity.ok(provaDto);
    }

    public ResponseEntity<DadosListagemProvaDTO> buscaProvaEspecifica(Long idProva, HttpServletRequest request) {
        Usuario usuario = usuarioService.buscaUsuario(request);
        Prova prova = provaRepository.getReferenceById(idProva);

        if (__listaUsuarioContemProvaBuscada(usuario.getProvas(), prova)) {
            return ResponseEntity.ok(new DadosListagemProvaDTO(prova));
        }
        return null;
    }

    private boolean __listaUsuarioContemProvaBuscada(List<Prova> listaProvas, Prova prova) {
        return listaProvas.stream().filter(p -> p.equals(prova)).toList().contains(prova);
    }

    public ResponseEntity<Void> deletarProvaPeloId(HttpServletRequest request, Long idProva) {
        Usuario usuario = usuarioService.buscaUsuario(request);
        Prova prova = provaRepository.getReferenceById(idProva);
        if (__listaUsuarioContemProvaBuscada(usuario.getProvas(), prova)) {
            provaRepository.delete(prova);
            usuario.deleteProvas(prova);
            
            return ResponseEntity.noContent().build();
        } 

        return null;
    }
}
