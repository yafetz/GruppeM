package Server.Repository;

import Server.Modell.Lernkartenset;
import Server.Modell.Nutzer;
import Server.Modell.Projektgruppe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LernkartensetRepository extends JpaRepository<Lernkartenset, Long> {

    Lernkartenset findLernkartensetById(long lernkartensetId);
    Lernkartenset findLernkartensetByBezeichnungAndProjektgruppe(String bezeichnung, Projektgruppe projektgruppe);
    List<Lernkartenset> getAllByProjektgruppe(Projektgruppe projektgruppe);
    List<Lernkartenset> getAllByProjektgruppeAndIstGeteilt(Projektgruppe projektgruppe, boolean istGeteilt);
    List<Lernkartenset> getAllByProjektgruppeAndErsteller(Projektgruppe projektgruppe, Nutzer ersteller);
    List<Lernkartenset> getAllByProjektgruppeAndErstellerAndIstGeteilt(Projektgruppe projektgruppe, Nutzer ersteller, boolean istGeteilt);

}
