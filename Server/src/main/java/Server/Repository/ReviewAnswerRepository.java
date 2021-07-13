package Server.Repository;


import Server.Modell.ReviewAnswer;
import Server.Modell.ReviewQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewAnswerRepository extends JpaRepository<ReviewAnswer, Long> {
    List<ReviewAnswer> findAllByQuestion(ReviewQuestion question);
    ReviewAnswer findById(long id);

}
