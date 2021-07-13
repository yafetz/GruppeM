package Server.Repository;

import Server.Modell.Nutzer;
import Server.Modell.ReviewBearbeitetQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewBearbeitetQuestionRepository extends JpaRepository<ReviewBearbeitetQuestion, Long> {
    @Query("SELECT COUNT(rwq) FROM ReviewBearbeitetQuestion rwq WHERE rwq.question.id = ?1 AND rwq.nutzer.id = ?2")
    int getAntwortenCountByQuestionIdAndNutzerId(long questionId, long nutzerId);

    @Query("SELECT COUNT(rwq) FROM ReviewBearbeitetQuestion rwq WHERE rwq.reviewAnswer.id = ?1 AND rwq.nutzer.id = ?2")
    int getEineAntwortCountByAnswerIdAndNutzerId(long answerId, long nutzerId);

}
