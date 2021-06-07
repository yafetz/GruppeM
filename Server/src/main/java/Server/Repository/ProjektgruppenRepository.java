package Server.Repository;

import Server.Modell.Lehrveranstaltung;
import Server.Modell.Projektgruppe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjektgruppenRepository extends JpaRepository<Projektgruppe, Long> {
    List<Projektgruppe> findAllByLehrveranstaltung(Lehrveranstaltung lehrveranstaltung);
    Projektgruppe findProjektgruppeById(long id);
    List<Projektgruppe> findAllByTitelEquals(String suchbegriff);
    Projektgruppe findProjektgruppeByTitel(String titel);
    List<Projektgruppe> findAllByLehrveranstaltungId(Long lvId);

}
