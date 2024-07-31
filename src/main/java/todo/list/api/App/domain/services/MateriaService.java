package todo.list.api.App.domain.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import todo.list.api.App.domain.dto.materia.DadosAlteracaoMateriaDTO;
import todo.list.api.App.domain.dto.materia.DadosCriacaoMateriaDTO;
import todo.list.api.App.domain.dto.materia.DadosListagemMateriaDTO;
import todo.list.api.App.domain.model.Assunto;
import todo.list.api.App.domain.model.Materia;
import todo.list.api.App.domain.model.Prova;
import todo.list.api.App.domain.model.Usuario;
import todo.list.api.App.domain.repository.MateriaRepository;

@Service
public class MateriaService {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private MateriaRepository materiaRepository;
    @Autowired
    private ProvaService provaService;

    public ResponseEntity<Page<DadosListagemMateriaDTO>> buscaMaterias(HttpServletRequest request, Long idProva, Pageable pageable) {

        if (__estaProvaPertenceAEsteUsuarioOuMateriaJaExiste(request, idProva, null)) {
            Page<DadosListagemMateriaDTO> listaDeMaterias = materiaRepository.findByProvaId(pageable, idProva)
                    .map(DadosListagemMateriaDTO::new);
            return ResponseEntity.ok(listaDeMaterias);
        }
        return ResponseEntity.badRequest().build();
    }

    public ResponseEntity<DadosListagemMateriaDTO> inserirMaterias(@Valid DadosCriacaoMateriaDTO dadosMateria, Long idProva, HttpServletRequest request) {
        Prova prova = provaService.buscaProvaPeloId(idProva);

        if (__estaProvaPertenceAEsteUsuarioOuMateriaJaExiste(request, idProva, dadosMateria)) {
            Materia materia = new Materia(dadosMateria);
            materia.setProva(prova);

            if (dadosMateria.listaDeAssuntos() != null && !dadosMateria.listaDeAssuntos().isEmpty()) {
                List<Assunto> listaAssuntos = dadosMateria.listaDeAssuntos().stream()
                        .map(Assunto::new)
                        .collect(Collectors.toList());

                materia.setAssuntos(listaAssuntos);

                listaAssuntos.forEach(assunto -> assunto.setMateria(materia));
                listaAssuntos.forEach(System.out::println);
            }
            materiaRepository.save(materia);

            return ResponseEntity.ok(new DadosListagemMateriaDTO(materia));
        }
        return ResponseEntity.badRequest().build();
    }

    public Materia buscaMateriaEspecifica(Long id) {
        return materiaRepository.getReferenceById(id);
    }

    private boolean __estaProvaPertenceAEsteUsuarioOuMateriaJaExiste(HttpServletRequest request, Long idProva, DadosCriacaoMateriaDTO dadosCriacaoMateriaDTO) {
        if (dadosCriacaoMateriaDTO != null) {
            boolean materiaJaExiste = __materiaJaExiste(dadosCriacaoMateriaDTO, idProva);
            if (materiaJaExiste) {
                return false;
            }
        }
        Usuario usuario = usuarioService.buscaUsuario(request);
        List<Prova> listaDeProvas = usuario.getProvas();
        Prova prova = provaService.buscaProvaPeloId(idProva);

        return listaDeProvas.contains(prova);

    }

    private boolean __materiaJaExiste(DadosCriacaoMateriaDTO dadosCriacaoMateriaDTO, Long idProva) {
        Prova prova = provaService.buscaProvaPeloId(idProva);
        List<String> listaDeNomesMaterias = prova.getListaDeMaterias().stream().map(Materia::getNome).toList();

        return listaDeNomesMaterias.contains(dadosCriacaoMateriaDTO.nome());
    }

    public ResponseEntity<Void> deletarMateria(HttpServletRequest request, Long idMateria) {
        Materia materia = materiaRepository.getReferenceById(idMateria);
        Usuario usuario = usuarioService.buscaUsuario(request);
        boolean materiaPertenceAoUsuario = usuarioService.verificaSeMateriaPertenceAUsuario(usuario, materia);

        if (materiaPertenceAoUsuario) {
            materiaRepository.delete(materia);
            Prova prova = materia.getProva();
            prova.removeMateria(materia);
            usuario.removerTodosOsPlanejamentosDaMateria(materia);

            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    public ResponseEntity<DadosListagemMateriaDTO> alteraMateria(HttpServletRequest request, DadosAlteracaoMateriaDTO dadosAlteracaoMateriaDTO, Long idMateria) {
        Usuario usuario = usuarioService.buscaUsuario(request);
        Materia materia = materiaRepository.getReferenceById(idMateria);
        boolean materiaPertenceAoUsuario = usuarioService.verificaSeMateriaPertenceAUsuario(usuario, materia);

        if(materiaPertenceAoUsuario) {
            materia.setNome(dadosAlteracaoMateriaDTO.nome());
            return ResponseEntity.ok(new DadosListagemMateriaDTO(materia));
        }
        return ResponseEntity.badRequest().build();
    }
}
