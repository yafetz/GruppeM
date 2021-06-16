package Server.Repository;

import Server.Modell.Lehrveranstaltung;
import Server.Modell.Nutzer;
import Server.Modell.Termin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KalenderRepository extends JpaRepository<Termin, Long> {
    List<Termin> findAllByNutzerId(Nutzer nutzer);
}
