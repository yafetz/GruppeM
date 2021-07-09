package Server.Repository;

import Server.Modell.ReviewBearbeitetQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewBearbeitetQuestionRepository extends JpaRepository<ReviewBearbeitetQuestion, Long> {
}
