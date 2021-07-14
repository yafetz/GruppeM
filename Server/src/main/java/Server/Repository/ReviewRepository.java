package Server.Repository;

import Server.Modell.Lehrveranstaltung;
import Server.Modell.Quiz;
import Server.Modell.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Review findById(long id);
    Review findByLehrveranstaltung(Lehrveranstaltung lehrveranstaltung);

}
