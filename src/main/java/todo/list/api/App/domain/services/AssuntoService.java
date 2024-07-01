package todo.list.api.App.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import todo.list.api.App.domain.dto.assunto.DadosAlteracaoAssuntoDTO;
import todo.list.api.App.domain.dto.assunto.DadosCriacaoAsssuntoDTO;
import todo.list.api.App.domain.dto.assunto.DadosDetalhamentoAssuntoDTO;
import todo.list.api.App.domain.dto.assunto.DadosListagemAssuntoDTO;
import todo.list.api.App.domain.model.Assunto;
import todo.list.api.App.domain.model.Materia;
import todo.list.api.App.domain.model.Usuario;
import todo.list.api.App.domain.repository.AssuntoRepository;

@Service
public class AssuntoService {

    @Autowired
    private AssuntoRepository assuntoRepository;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private MateriaService materiaService;

    public ResponseEntity<Page<DadosListagemAssuntoDTO>> buscaAssuntos(@PageableDefault(size = 5, page = 0, sort = {"nome"}) Pageable pageable, HttpServletRequest request, @PathVariable Long idMateria) {
        boolean materiaPertenceAUsuario = __verificaSeMateriaPertenceAUsuario(request, idMateria);

        if (materiaPertenceAUsuario) {
            Page<DadosListagemAssuntoDTO> listaAssuntosDTOs = assuntoRepository.findByMateriaId(pageable, idMateria)
                    .map(DadosListagemAssuntoDTO::new);

            return ResponseEntity.ok(listaAssuntosDTOs);
        }
        return ResponseEntity.badRequest().build();
    }

    public ResponseEntity<DadosListagemAssuntoDTO> criarAssunto(@RequestBody @Valid DadosCriacaoAsssuntoDTO dadosCriacaoAsssuntoDTO, HttpServletRequest request, @PathVariable Long idMateria) {
        boolean materiaPertenceAUsuario = __verificaSeMateriaPertenceAUsuario(request, idMateria);

        if (materiaPertenceAUsuario) {
            Assunto assunto = new Assunto(dadosCriacaoAsssuntoDTO);
            Materia materia = materiaService.buscaMateriaEspecifica(idMateria);
            materia.setAssunto(assunto);
            assunto.setMateria(materia);
            assuntoRepository.save(assunto);
            return ResponseEntity.ok(new DadosListagemAssuntoDTO(assunto));
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
            if (dadosAlteracaoAssuntoDTO.quantidadeDePdf() != 0) {
                assunto.setQuantidadePdf(dadosAlteracaoAssuntoDTO.quantidadeDePdf());
            }

            return ResponseEntity.ok(new DadosDetalhamentoAssuntoDTO(assunto));
        }
        return ResponseEntity.badRequest().build();
    }
}
