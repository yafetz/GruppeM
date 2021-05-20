package Server.Repository;

import Server.Modell.Lehrveranstaltung;
import Server.Modell.Nutzer;
import Server.Modell.Student;
import Server.Modell.TeilnehmerListe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeilnehmerListeRepository extends JpaRepository<TeilnehmerListe,Long> {

    List<TeilnehmerListe> findAllByLehrveranstaltung(Lehrveranstaltung lehrveranstaltung);
    List<TeilnehmerListe> findDistinctByLehrveranstaltung(Lehrveranstaltung lehrveranstaltung);
    List<TeilnehmerListe> getDistinctByLehrveranstaltung(Lehrveranstaltung lehrveranstaltung);
    List<TeilnehmerListe> findAllByNutzerId(Nutzer nutzer);

    Boolean existsByLehrveranstaltungAndNutzerId(Lehrveranstaltung lehrveranstaltung, Nutzer nutzer);

}

