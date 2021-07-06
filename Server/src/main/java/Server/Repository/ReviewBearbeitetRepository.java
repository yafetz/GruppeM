package Server.Repository;

import Server.Modell.ReviewBearbeitet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewBearbeitetRepository extends JpaRepository<ReviewBearbeitet, Long> {

}
