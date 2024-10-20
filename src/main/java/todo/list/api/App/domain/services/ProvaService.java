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
import todo.list.api.App.domain.repository.EstatisticaQuestaoRepository;
import todo.list.api.App.domain.repository.ProvaRepository;

@Service
public class ProvaService {

    @Autowired
    private ProvaRepository provaRepository;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private EstatisticaQuestaoRepository estatisticaQuestaoRepository;

    public ResponseEntity<Page<DadosListagemProvaDTO>> buscarProvas(@PageableDefault(size = 10, page = 0, sort = {"dataDaProva"}) Pageable pageable, HttpServletRequest request) {
        Long id = usuarioService.buscaUsuario(request).getId();
        if (id != null) {
            Page<DadosListagemProvaDTO> listaDeProvas = provaRepository.findAllByUsuarioId(pageable, id)
                    .map(DadosListagemProvaDTO::new);

            return ResponseEntity.ok(listaDeProvas);
        }
        return ResponseEntity.badRequest().build();
    }

    public ResponseEntity<DadosListagemProvaDTO> inserirProva(@Valid DadosCriacaoProvaDTO dadosProva, HttpServletRequest request) throws Exception {
        boolean provaJaCadastrada = buscaProvaPorTituloParaNaoHaverDuplicidade(dadosProva, request);

        if (!provaJaCadastrada) {
            Usuario usuario = usuarioService.buscaUsuario(request);
            Prova prova = new Prova(dadosProva);
            prova.setUsuario(usuario);
            usuario.setProvas(prova);

            DadosListagemProvaDTO provaDto = new DadosListagemProvaDTO(provaRepository.findByTituloAndUsuarioId(dadosProva.titulo(), usuario.getId()));
            return ResponseEntity.ok(provaDto);
        }
        throw new Exception("Prova já cadastrada");

    }

    public ResponseEntity<DadosDetalhamentoProvaDTO> buscaProvaEspecifica(Long idProva, HttpServletRequest request) {
        Usuario usuario = usuarioService.buscaUsuario(request);
        Prova prova = provaRepository.getReferenceById(idProva);

        if (__listaUsuarioContemProvaBuscada(usuario.getProvas(), prova)) {
            return ResponseEntity.ok(new DadosDetalhamentoProvaDTO(prova, estatisticaQuestaoRepository));
        }
        return ResponseEntity.badRequest().build();
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

        return ResponseEntity.badRequest().build();
    }

    private boolean buscaProvaPorTituloParaNaoHaverDuplicidade(DadosCriacaoProvaDTO dadosCriacaoProvaDTO, HttpServletRequest request) {
        Usuario usuario = usuarioService.buscaUsuario(request);
        return usuario.getProvas().contains(new Prova(dadosCriacaoProvaDTO));
    }

    public Prova buscaProvaPeloId(Long id) {
        return provaRepository.getReferenceById(id);
    }

    public ResponseEntity<DadosListagemProvaDTO> atualizarProva(Long idProva,
            @Valid DadosCriacaoProvaDTO dadosProva, HttpServletRequest request) {
        Usuario usuario = usuarioService.buscaUsuario(request);
        Prova prova = provaRepository.findById(idProva).orElseThrow(() -> new RuntimeException("Prova não encontrada"));
        boolean provaPertenceAoUsuario = usuario.getProvas().contains(prova);

        if (provaPertenceAoUsuario) {
            prova.setTitulo(dadosProva.titulo());
            prova.setDataDaProva(dadosProva.dataDaProva());
            prova.setHexadecimalCorProva(dadosProva.corDaProva());

            Prova provaDoUsuario = usuario.getProvas().stream()
                    .filter(p -> p.getId().equals(idProva))
                    .toList().get(0);
            
                    provaDoUsuario.setTitulo(dadosProva.titulo());
                    provaDoUsuario.setDataDaProva(dadosProva.dataDaProva());

            return ResponseEntity.ok(new DadosListagemProvaDTO(provaRepository.save(prova)));
        }
        return ResponseEntity.badRequest().build();
    }
}
