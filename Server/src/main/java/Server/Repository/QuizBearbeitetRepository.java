package Server.Repository;

import Server.Modell.Nutzer;
import Server.Modell.QuizBearbeitet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface QuizBearbeitetRepository extends JpaRepository<QuizBearbeitet,Long> {
    @Query("SELECT COUNT( DISTINCT qb.nutzer) FROM QuizBearbeitet qb WHERE qb.quiz.id LIKE ?1 ")
    int getAllStudent(long quiz_id);
    @Query("SELECT COUNT(DISTINCT qb.nutzer) FROM QuizBearbeitet qb WHERE qb.bestanden LIKE true and qb.quiz.id LIKE ?1  ")
    int getAllStudentPassed(long quiz_id);

    @Query("SELECT COUNT( qb.nutzer) FROM QuizBearbeitet qb WHERE qb.quiz.id LIKE ?1 and qb.nutzer.id LIKE ?2 GROUP BY qb.nutzer ")
    List<Integer> getAllStudentVersuche(long quiz_id, long nutzer_Id);


}
