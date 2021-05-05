package Server.Repository;

import Server.Modell.Lehrender;
import Server.Modell.Lehrveranstaltung;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LehrveranstaltungRepository extends JpaRepository<Lehrveranstaltung,Long> {

}
