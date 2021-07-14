package Server.Repository;

import Server.Modell.Nutzer;
import Server.Modell.QuizBearbeitet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface QuizBearbeitetRepository extends JpaRepository<QuizBearbeitet,Long> {
    @Query("SELECT COUNT( DISTINCT qb.nutzer) FROM QuizBearbeitet qb WHERE qb.quiz.id = ?1 ")
    int getAllStudent(long quiz_id);
    @Query("SELECT COUNT(DISTINCT qb.nutzer) FROM QuizBearbeitet qb WHERE qb.quiz.id = ?1 AND qb.bestanden=true ")
    int getAllStudentPassed(long quiz_id);

    @Query("SELECT qb.nutzer,  COUNT( qb.nutzer) as Anzahl FROM QuizBearbeitet qb WHERE qb.quiz.id = ?1  GROUP BY qb.nutzer.id ")
    List<Object[]> getAllStudentVersuche(long quiz_id);
    @Query("SELECT COUNT( DISTINCT qb.quiz.id) FROM QuizBearbeitet qb WHERE qb.nutzer.id = ?1 AND qb.quiz.lehrveranstaltung.id = ?2" )
    float getNutzerquiz(long nutzerid, long lehrveranstaltungsid);

    //@Query("SELECT qb.nutzer,  COUNT( qb.nutzer) as Anzahl FROM QuizBearbeitet qb WHERE qb.quiz.id LIKE ?1  GROUP BY qb.nutzer.id ")
   // List<Object[]> getAllStudentVersuche(long quiz_id);

    @Query("SELECT COUNT(DISTINCT qb.nutzer) FROM QuizBearbeitet qb WHERE qb.quiz.id LIKE ?1 AND qb.bestanden=true ")
    int getAllKursePassedSommersemester(long quiz_id);
    @Query("SELECT COUNT(DISTINCT qb.quiz.id) FROM QuizBearbeitet qb WHERE qb.nutzer.id = ?1 AND qb.bestanden=true AND qb.quiz.lehrveranstaltung.id = ?2" )
    float getAnzahlQuizBestanden(long nutzerid, long lehrveranstaltungsid);

   // @Query("SELECT COUNT(DISTINCT qb.quiz) FROM QuizBearbeitet qb WHERE qb.nutzer.id LIKE ?1 AND qb.bestanden=true AND qb.lehrveranstaltung.id like ?2 ")
    //int getAllAnzahlPassedProStudent(long quiz_id, long lehrveranstaltungs_id);

    @Query("SELECT DISTINCT qb.bestanden FROM QuizBearbeitet qb WHERE qb.quiz.id LIKE ?1 AND qb.nutzer.id LIKE ?2 AND qb.bestanden=true ")
    Boolean getBestandenOderNicht(long quiz_id, long nutzer_id);
}
