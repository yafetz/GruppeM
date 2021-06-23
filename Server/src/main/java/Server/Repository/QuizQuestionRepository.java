package Server.Repository;

import Server.Modell.Quiz;
import Server.Modell.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {
    List<QuizQuestion> findAllByQuiz(Quiz quiz);
    QuizQuestion findById(long id);
}
