package todo.list.api.App.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import todo.list.api.App.domain.model.Questao;

public interface QuestaoRepository extends JpaRepository<Questao, Long>{

}
