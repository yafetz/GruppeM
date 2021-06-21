package Server.Repository;


import Server.Modell.Lehrveranstaltung;
import Server.Modell.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findAllByLehrveranstaltung(Lehrveranstaltung lehrveranstaltungsId);
    Quiz findById(long id);
}