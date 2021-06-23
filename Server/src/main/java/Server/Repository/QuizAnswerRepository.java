package Server.Repository;

import Server.Modell.QuizAnswer;
import Server.Modell.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizAnswerRepository extends JpaRepository<QuizAnswer, Long> {
    List<QuizAnswer> findAllByQuestion(QuizQuestion question);
}
