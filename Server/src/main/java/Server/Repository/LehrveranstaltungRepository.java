package Server.Repository;

import Server.Modell.Lehrender;
import Server.Modell.Lehrveranstaltung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LehrveranstaltungRepository extends JpaRepository<Lehrveranstaltung,Long> {
    Lehrveranstaltung findAllById(long id);

}
