package Server.Repository;

import Server.Modell.Review;
import Server.Modell.ReviewQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ReviewQuestionRepository extends JpaRepository<ReviewQuestion, Long> {
    List<ReviewQuestion> findAllByReview(Review review);
    ReviewQuestion findById(long id);
}
