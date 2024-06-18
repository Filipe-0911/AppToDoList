package todo.list.api.App.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import todo.list.api.App.domain.dto.mediaquestoes.DadosDetalhamentoMediaQuestoesDTO;
import todo.list.api.App.domain.services.QuestaoService;

@RestController
@RequestMapping("/mediaquestoes")
public class MediaQuestoesController {
    @Autowired
    private QuestaoService questaoService;
    
    @GetMapping("/{idProva}")
    public ResponseEntity<Page<DadosDetalhamentoMediaQuestoesDTO>> buscaDadosMediaQuestoes(@PageableDefault(size=5, page=0, sort = {"nome"})Pageable pageable, @PathVariable Long idProva ,HttpServletRequest request) {
        return questaoService.buscaDadosMediaQuestoes(pageable, request, idProva);
    }
}
