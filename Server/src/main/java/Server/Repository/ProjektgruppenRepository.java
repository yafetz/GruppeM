package Server.Repository;

import Server.Modell.Lehrveranstaltung;
import Server.Modell.Projektgruppe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjektgruppenRepository extends JpaRepository<Projektgruppe, Long> {
    Projektgruppe findProjektgruppeById(long id);
    Projektgruppe findProjektgruppeByTitel(String titel);
    List<Projektgruppe> findAllByLehrveranstaltung(Lehrveranstaltung lehrveranstaltung);
    List<Projektgruppe> findAllByTitelEquals(String suchbegriff);
    List<Projektgruppe> findAllByLehrveranstaltungId(Long lvId);

    @Query("SELECT projektgruppe FROM Projektgruppe projektgruppe WHERE projektgruppe.lehrveranstaltung.id = ?1 AND projektgruppe.titel LIKE %?2%")
    List<Projektgruppe> getAllProjektgruppeByLehrveranstaltungAndKeyword(Long lvID, String keyword);
}
