package Server.Repository;

import Server.Modell.Nutzer;
import Server.Modell.ReviewBearbeitetQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewBearbeitetQuestionRepository extends JpaRepository<ReviewBearbeitetQuestion, Long> {
    List<Nutzer> findAllByQuestionId(long questionId);
}
