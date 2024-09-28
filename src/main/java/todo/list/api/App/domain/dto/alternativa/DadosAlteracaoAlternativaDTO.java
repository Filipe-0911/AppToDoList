package todo.list.api.App.domain.dto.alternativa;

public record DadosAlteracaoAlternativaDTO(
        Long id,
        String textoAlternativa,
        boolean ehCorreta
) {
}
