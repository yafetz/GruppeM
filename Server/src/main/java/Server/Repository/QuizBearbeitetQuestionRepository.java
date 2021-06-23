package Server.Repository;

import Server.Modell.QuizBearbeitetQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuizBearbeitetQuestionRepository extends JpaRepository<QuizBearbeitetQuestion,Long> {

    @Query("SELECT COUNT( qb.question) FROM QuizBearbeitetQuestion qb WHERE qb.question.quiz.id LIKE ?1 AND qb.korrekt LIKE true GROUP BY qb.question ")
    List<Integer> getAllStudentRichtigeAntwort(long questionId);

}
