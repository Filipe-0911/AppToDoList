package todo.list.api.App.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
import todo.list.api.App.domain.services.EstatisticaQuestoesService;

@RestController
@RequestMapping("/media-questoes")
@SecurityRequirement(name =  "bearer-key")
public class MediaQuestoesController {
    @Autowired
    private EstatisticaQuestoesService estatisticaQuestoesService;
    
    @GetMapping("/{idProva}")
    public ResponseEntity<Page<DadosDetalhamentoMediaQuestoesDTO>> buscaDadosMediaQuestoes(@PageableDefault(size=5, page=0, sort = {"nome"})Pageable pageable, @PathVariable Long idProva ,HttpServletRequest request) {
        return estatisticaQuestoesService.buscaDadosMediaQuestoes(pageable, request, idProva);
    }
}
