package Server.Repository;

import Server.Modell.Nutzer;
import Server.Modell.ReviewBearbeitet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewBearbeitetRepository extends JpaRepository<ReviewBearbeitet, Long> {
    List<ReviewBearbeitet> findByNutzer(Nutzer nutzer);


}
