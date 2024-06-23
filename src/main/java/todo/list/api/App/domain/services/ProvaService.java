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

    public ResponseEntity<Page<DadosListagemProvaDTO>> buscarProvas(@PageableDefault(size=10, page=0, sort = {"dataDaProva"})Pageable pageable, HttpServletRequest request) {
        Long id = usuarioService.buscaUsuario(request).getId();
        if (id != null) {
            Page<DadosListagemProvaDTO> listaDeProvas = provaRepository.findAllByUsuarioId(pageable, id)
                .map(DadosListagemProvaDTO::new);

            return ResponseEntity.ok(listaDeProvas);
        }
        return null;
    }
    
    public ResponseEntity<DadosListagemProvaDTO> inserirProva(@Valid DadosCriacaoProvaDTO dadosProva, HttpServletRequest request) throws Exception {
        boolean provaJaCadastrada = buscaProvaPorTituloParaNaoHaverDuplicidade(dadosProva);

        if (!provaJaCadastrada) {
            Usuario usuario = usuarioService.buscaUsuario(request);
            Prova prova = new Prova(dadosProva);
            prova.setUsuario(usuario);
            usuario.setProvas(prova);

            DadosListagemProvaDTO provaDto = new DadosListagemProvaDTO(provaRepository.findByTitulo(dadosProva.titulo()));
            return ResponseEntity.ok(provaDto);
        }
        throw new Exception("Prova já cadastrada");

    }

    public ResponseEntity<DadosDetalhamentoProvaDTO> buscaProvaEspecifica(Long idProva, HttpServletRequest request) {
        Usuario usuario = usuarioService.buscaUsuario(request);
        Prova prova = provaRepository.getReferenceById(idProva);

        if (__listaUsuarioContemProvaBuscada(usuario.getProvas(), prova)) {
            return ResponseEntity.ok(new DadosDetalhamentoProvaDTO(prova));
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
            // antes de remover a prova, devem ser removidos todos os planejamentos referentes a esta prova
            usuario.removerTodosOsPlanejamentosDaProva(prova);
            provaRepository.delete(prova);
            usuario.deleteProvas(prova);
            
            return ResponseEntity.noContent().build();
        } 

        return null;
    }

    private boolean buscaProvaPorTituloParaNaoHaverDuplicidade(DadosCriacaoProvaDTO dadosCriacaoProvaDTO) {
        Prova prova = provaRepository.findByTitulo(dadosCriacaoProvaDTO.titulo());
        return prova != null;
    }

    public Prova buscaProvaPeloId(Long id) {
        return provaRepository.getReferenceById(id);
    }
}
