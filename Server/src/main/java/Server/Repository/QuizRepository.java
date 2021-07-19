package Server.Repository;


import Server.Modell.Lehrveranstaltung;
import Server.Modell.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findAllByLehrveranstaltung(Lehrveranstaltung lehrveranstaltungsId);
    Quiz findById(long id);
    @Query("SELECT COUNT(quiz.id) FROM Quiz quiz WHERE quiz.lehrveranstaltung.id = ?1")
    int getQuizCount(long lehrveranstaltungsid);

   @Query("SELECT COUNT(DISTINCT q.id) FROM Quiz q WHERE q.lehrveranstaltung.id LIKE ?1 ")
    int getAllAmountOfQuizOfCourse(long lehrveranstaltungs_id);

    @Query("SELECT q.id FROM Quiz q WHERE q.lehrveranstaltung.id LIKE ?1 ")
    List<Integer> getAllIDsFromQuizFromACourse(long lehrveranstaltungs_id);

}
