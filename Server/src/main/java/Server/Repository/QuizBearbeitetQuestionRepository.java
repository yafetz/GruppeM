package Server.Repository;

import Server.Modell.QuizBearbeitetQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizBearbeitetQuestionRepository extends JpaRepository<QuizBearbeitetQuestion,Long> {
    @Query("SELECT COUNT( qb.question) FROM QuizBearbeitetQuestion qb WHERE qb.question.quiz.id = ?1 AND qb.korrekt = true GROUP BY qb.question ")
    List<Integer> getAllStudentRichtigeAntworten(long quiz_id);

    //SELECT question_id FROM quizbearbeitenquestion WHERE  korrekt = 1
    //@Query("SELECT qb.question.id FROM QuizBearbeitetQuestion qb WHERE qb.korrekt LIKE true")
    @Query("SELECT qb.question.question FROM QuizBearbeitetQuestion qb WHERE qb.korrekt = TRUE AND qb.question.quiz.id = ?1")
    List<Object[]> getAllStudentRichtigeAntwort(long quizId);



}
