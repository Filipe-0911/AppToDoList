package todo.list.api.App.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import todo.list.api.App.domain.model.AlternativaQuestao;

public interface AlternativaRepository extends JpaRepository<AlternativaQuestao, Long> {
}
