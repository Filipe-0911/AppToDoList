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
    private ProvaRepository provaRepository;

    public ResponseEntity<Page<DadosListagemMateriaDTO>> buscaMaterias(HttpServletRequest request, Long idProva, Pageable pageable) {

        if (__estaProvaPertenceAEsteUsuario(request, idProva)) {
            Page<DadosListagemMateriaDTO> listaDeMaterias = materiaRepository.findByProvaId(pageable, idProva)
                    .map(DadosListagemMateriaDTO::new);
            return ResponseEntity.ok(listaDeMaterias);
        }
        return null;
    }

    public ResponseEntity<DadosListagemMateriaDTO> inserirMaterias(@Valid DadosCriacaoMateriaDTO dadosMateria, Long idProva, HttpServletRequest request) {
        Prova prova = __buscaProvaPeloId(idProva);

        if (__estaProvaPertenceAEsteUsuario(request, idProva)) {
            Materia materia = new Materia(dadosMateria);
            materia.setProva(prova);
            prova.setListaDeMaterias(materia);

            return ResponseEntity.ok(new DadosListagemMateriaDTO(materia));
        }
        return null;
    }
    
    public Materia buscaMateriaEspecifica(Long id) {
        return materiaRepository.getReferenceById(id);
    }

    private boolean __estaProvaPertenceAEsteUsuario(HttpServletRequest request, Long idProva) {
        Usuario usuario = usuarioService.buscaUsuario(request);
        List<Prova> listaDeProvas = usuario.getProvas();
        Prova prova = __buscaProvaPeloId(idProva);

        return listaDeProvas.contains(prova);

    }

    private Prova __buscaProvaPeloId(Long idProva) {
        return provaRepository.getReferenceById(idProva);
    }

}
