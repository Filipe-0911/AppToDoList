package todo.list.api.App.domain.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import todo.list.api.App.domain.dto.materia.DadosCriacaoMateriaDTO;
import todo.list.api.App.domain.dto.materia.DadosListagemMateriaDTO;
import todo.list.api.App.domain.model.Materia;
import todo.list.api.App.domain.model.Prova;
import todo.list.api.App.domain.model.Usuario;
import todo.list.api.App.domain.repository.MateriaRepository;
import todo.list.api.App.domain.repository.ProvaRepository;

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
        return null;
    }

    public ResponseEntity<DadosListagemMateriaDTO> inserirMaterias(@Valid DadosCriacaoMateriaDTO dadosMateria, Long idProva, HttpServletRequest request) {
        Prova prova = provaService.buscaProvaPeloId(idProva);

        if (__estaProvaPertenceAEsteUsuarioOuMateriaJaExiste(request, idProva, dadosMateria)) {
            Materia materia = new Materia(dadosMateria);
            materia.setProva(prova);
            prova.setListaDeMaterias(materia);

            return ResponseEntity.ok(new DadosListagemMateriaDTO(materiaRepository.findByNome(dadosMateria.nome())));
        }
        return ResponseEntity.badRequest().build();
    }

    public Materia buscaMateriaEspecifica(Long id) {
        return materiaRepository.getReferenceById(id);
    }

    private boolean __estaProvaPertenceAEsteUsuarioOuMateriaJaExiste(HttpServletRequest request, Long idProva, DadosCriacaoMateriaDTO dadosCriacaoMateriaDTO) {
        if (dadosCriacaoMateriaDTO != null) {
            boolean materiaJaExiste = __materiaJaExiste(dadosCriacaoMateriaDTO, idProva);
            if (materiaJaExiste) return false;
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

}
