package todo.list.api.App.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import todo.list.api.App.domain.dto.assunto.*;
import todo.list.api.App.domain.model.Assunto;
import todo.list.api.App.domain.model.Materia;
import todo.list.api.App.domain.model.Prova;
import todo.list.api.App.domain.model.Usuario;
import todo.list.api.App.domain.repository.AssuntoRepository;

import java.util.List;

@Service
public class AssuntoService {

    @Autowired
    private AssuntoRepository assuntoRepository;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private MateriaService materiaService;
    @Autowired
    private PageableService pageableService;

    public ResponseEntity<Page<DadosListagemAssuntoDTO>> buscaAssuntos(@PageableDefault(size = 5, page = 0, sort = {"nome"}) Pageable pageable, HttpServletRequest request, @PathVariable Long idMateria) {
        boolean materiaPertenceAUsuario = __verificaSeMateriaPertenceAUsuario(request, idMateria);

        if (materiaPertenceAUsuario) {
            Page<DadosListagemAssuntoDTO> listaAssuntosDTOs = assuntoRepository.findByMateriaId(pageable, idMateria)
                    .map(DadosListagemAssuntoDTO::new);

            return ResponseEntity.ok(listaAssuntosDTOs);
        }
        return ResponseEntity.badRequest().build();
    }

    public ResponseEntity<DadosDetalhamentoAssuntoDTO> criarAssunto(@RequestBody @Valid DadosCriacaoAsssuntoDTO dadosCriacaoAsssuntoDTO, HttpServletRequest request, @PathVariable Long idMateria) {
        boolean materiaPertenceAUsuario = __verificaSeMateriaPertenceAUsuario(request, idMateria);

        if (materiaPertenceAUsuario) {
            Assunto assunto = new Assunto(dadosCriacaoAsssuntoDTO);
            Materia materia = materiaService.buscaMateriaEspecifica(idMateria);
            materia.setAssunto(assunto);
            assunto.setMateria(materia);
            assuntoRepository.save(assunto);
            return ResponseEntity.ok(new DadosDetalhamentoAssuntoDTO(assunto));
        }
        return ResponseEntity.badRequest().build();
    }

    public ResponseEntity<DadosDetalhamentoAssuntoDTO> buscaAssuntoEspecifico(@PathVariable Long idMateria, @PathVariable Long idAssunto, HttpServletRequest request) {
        boolean materiaPertenceAUsuario = __verificaSeMateriaPertenceAUsuario(request, idMateria);
        if (materiaPertenceAUsuario) {
            Assunto assunto = assuntoRepository.getReferenceById(idAssunto);
            return ResponseEntity.ok(new DadosDetalhamentoAssuntoDTO(assunto));
        }
        return ResponseEntity.badRequest().build();

    }

    public ResponseEntity<Void> deletarAssunto(@PathVariable Long idMateria, @PathVariable Long idAssunto, HttpServletRequest request) {
        boolean materiapertenceAUsuario = __verificaSeMateriaPertenceAUsuario(request, idMateria);
        if (materiapertenceAUsuario) {
            Assunto assunto = assuntoRepository.getReferenceById(idAssunto);
            Materia materia = materiaService.buscaMateriaEspecifica(idMateria);

            assuntoRepository.delete(assunto);
            materia.deleteAssunto(assunto);

            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }

    public Assunto buscarAssuntoEspecificoSemParametrosDePath(Long id) {
        return assuntoRepository.getReferenceById(id);
    }


    private boolean __verificaSeMateriaPertenceAUsuario(HttpServletRequest request, Long idMateria) {
        Usuario usuario = usuarioService.buscaUsuario(request);
        Materia materia = materiaService.buscaMateriaEspecifica(idMateria);

        return usuarioService.verificaSeMateriaPertenceAUsuario(usuario, materia);
    }

    public ResponseEntity<DadosDetalhamentoAssuntoDTO> atualizaAssunto(Long idMateria, Long idAssunto, HttpServletRequest request, DadosAlteracaoAssuntoDTO dadosAlteracaoAssuntoDTO) {
        boolean materiaPertenceAUsuario = __verificaSeMateriaPertenceAUsuario(request, idMateria);

        if(materiaPertenceAUsuario) {
            Assunto assunto = assuntoRepository.getReferenceById(idAssunto);

            if (dadosAlteracaoAssuntoDTO.nome() != null) {
                assunto.setNome(dadosAlteracaoAssuntoDTO.nome());
            }
            if (dadosAlteracaoAssuntoDTO.quantidadePdf() != 0) {
                assunto.setQuantidadePdf(dadosAlteracaoAssuntoDTO.quantidadePdf());
            }

            return ResponseEntity.ok(new DadosDetalhamentoAssuntoDTO(assunto));
        }
        return ResponseEntity.badRequest().build();
    }

    public ResponseEntity<DadosDetalhamentoAssuntoDTO> insereComentarios(Long idMateria, Long idAssunto, HttpServletRequest request, DadosComentariosAssuntoDTO dadosComentariosAssuntoDTO) {
        boolean materiaPertenceAUsuario = __verificaSeMateriaPertenceAUsuario(request, idMateria);
        if(materiaPertenceAUsuario) {
            Assunto assunto = assuntoRepository.getReferenceById(idAssunto);

            assunto.setComentarios(dadosComentariosAssuntoDTO.comentarios());
            return ResponseEntity.ok(new DadosDetalhamentoAssuntoDTO(assunto));
        }
        return ResponseEntity.badRequest().build();
    }

    public ResponseEntity<DadosComentariosAssuntoDTO> alteraComentarios(Long idMateria, Long idAssunto, HttpServletRequest request, DadosComentariosAssuntoDTO dadosComentariosAssuntoDTO) {
        boolean materiaPertenceAUsuario = __verificaSeMateriaPertenceAUsuario(request, idMateria);
        if(materiaPertenceAUsuario) {
            Assunto assunto = assuntoRepository.getReferenceById(idAssunto);

            assunto.setComentarios(dadosComentariosAssuntoDTO.comentarios());
            return ResponseEntity.ok(new DadosComentariosAssuntoDTO(assunto));
        }
        return ResponseEntity.badRequest().build();
    }

    public ResponseEntity<?> buscaAssuntoPorNome(String nomeAssuntoRecebido, HttpServletRequest request) {
        Usuario usuario = usuarioService.buscaUsuario(request);
        String nomeAssunto = nomeAssuntoRecebido.replace("+", " ");
        Assunto assunto = assuntoRepository.findByNome(nomeAssunto);

        boolean assuntoPercenteAUsuario = usuarioService.verificaSeAssuntoPertenceAUsuario(usuario, assunto);

        if(assuntoPercenteAUsuario) {
            return ResponseEntity.ok(new DadosDetalhamentoAssuntoDTO(assunto));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Assunto não pertence ao usuário.");
    }

    public ResponseEntity<?> buscaAssuntosPorIdMateria(Long idMateria, HttpServletRequest request, @PageableDefault(size = 5, page = 0, sort = {"nome"}) Pageable pageable) {
        if(__verificaSeMateriaPertenceAUsuario(request, idMateria)) {
            Page<DadosDetalhamentoAssuntoDTO> assuntos = assuntoRepository.findByMateriaId(pageable, idMateria)
                    .map(DadosDetalhamentoAssuntoDTO::new);

            return ResponseEntity.ok(assuntos);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Matéria não pertence ao usuário.");
    }

    public ResponseEntity<Page<DadosListagemAssuntoDTO>> buscarTodosOsAssuntos(HttpServletRequest request, Pageable pageable) {
        Usuario usuario = usuarioService.buscaUsuario(request);
        List<Prova> listaDeProvas = usuario.getProvas();

        List<DadosListagemAssuntoDTO> listaDeAssuntoDto = listaDeProvas.stream()
                .flatMap(p -> p.getListaDeMaterias().stream())
                .flatMap(materia -> materia.getListaAssuntos().stream())
                .toList()
                .stream()
                .map(DadosListagemAssuntoDTO::new)
                .toList();

        return ResponseEntity.ok((Page<DadosListagemAssuntoDTO>) pageableService.createPageFromList(listaDeAssuntoDto, pageable));
    }
}
