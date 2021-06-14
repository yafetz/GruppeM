package Server.Repository;

import Server.Modell.Termin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KalenderRepository extends JpaRepository<Termin, Long> {
}
