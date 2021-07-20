package Server.Repository;

import Server.Modell.Nutzer;
import Server.Modell.Thema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ThemenRepository extends JpaRepository<Thema,Long> {
    @Query("select thema from Thema thema join TeilnehmerListe t On t.nutzerId = ?2 AND t.lehrveranstaltung = thema.lehrveranstaltung where thema.nutzer = ?1")
    List<Thema> findAllByNutzerWhereNotTeilnehmer(Nutzer nutzerLehrender, Nutzer anfragender);
    Thema findThemaById(Long id);
}
