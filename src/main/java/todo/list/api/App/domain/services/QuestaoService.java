package todo.list.api.App.domain.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import todo.list.api.App.domain.dto.mediaquestoes.DadosDetalhamentoMediaQuestoesDTO;
import todo.list.api.App.domain.dto.questao.DadosCriacaoQuestaoDTO;
import todo.list.api.App.domain.dto.questao.DadosDetalhamentoQuestaoDTO;
import todo.list.api.App.domain.model.Assunto;
import todo.list.api.App.domain.model.Prova;
import todo.list.api.App.domain.model.Questao;
import todo.list.api.App.domain.model.Usuario;
import todo.list.api.App.domain.repository.ProvaRepository;
import todo.list.api.App.domain.repository.QuestaoRepository;

@Service
public class QuestaoService {

    @Autowired
    private ProvaRepository provaRepository;

    @Autowired
    private QuestaoRepository questaoRepository;

    @Autowired
    private UsuarioService usuarioService;

    public ResponseEntity<DadosDetalhamentoQuestaoDTO> criarQuestao(DadosCriacaoQuestaoDTO questao, Assunto assunto) {
        Questao questaoBuscada = questaoRepository.findByDataPreenchimentoAndAssuntoId(questao.dataPreenchimento(), assunto.getId());

        if (questaoBuscada != null && assunto.getId().equals(questaoBuscada.getAssunto().getId())) {
            System.out.println("teste");
            questaoBuscada.setQuestoesAcertadas(questao.questoesAcertadas());
            questaoBuscada.setQuestoesFeitas(questao.questoesFeitas());
            return ResponseEntity.ok(new DadosDetalhamentoQuestaoDTO(questaoBuscada));
        }
        Questao questaoCriada = new Questao(questao);
        assunto.setQuestoes(questaoCriada);
        questaoCriada.setAssunto(assunto);
        questaoRepository.save(questaoCriada);

        return ResponseEntity.ok(new DadosDetalhamentoQuestaoDTO(questaoCriada));
    }

    public ResponseEntity<Page<DadosDetalhamentoMediaQuestoesDTO>> buscaDadosMediaQuestoes(@PageableDefault(size = 5, page = 0, sort = {"nome"}) Pageable pageable, HttpServletRequest request, Long idProva) {
        boolean estaprovaPertenceAEstaUsuario = __estaProvaPertenceAEsteUsuario(request, idProva);
        Usuario usuario = usuarioService.buscaUsuario(request);
        List<Long> idAssuntosUsuario = retornaListaFlat(usuario, idProva);

        if (estaprovaPertenceAEstaUsuario) {
            return ResponseEntity.ok(questaoRepository.calcularEstatisticasPorDia(pageable, idAssuntosUsuario));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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

    private List<Long> retornaListaFlat(Usuario usuario, Long idProva) {
        return usuario.getProvas().stream()
                .filter(p -> p.getId().equals(idProva))
                .flatMap(p -> p.getListaDeMaterias().stream()
                .flatMap(m -> m.getListaAssuntos().stream()
                .map(a -> a.getId())))
                .collect(Collectors.toList());
    }
}
