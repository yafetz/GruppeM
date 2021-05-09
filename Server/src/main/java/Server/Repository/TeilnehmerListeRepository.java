package Server.Repository;

import Server.Modell.Lehrveranstaltung;
import Server.Modell.Student;
import Server.Modell.TeilnehmerListe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeilnehmerListeRepository extends JpaRepository<TeilnehmerListe,Long> {
   // TeilnehmerListe findAllByLehrveranstaltung(Lehrveranstaltung lehrveranstaltung);
    TeilnehmerListe getAllByLehrveranstaltung(Lehrveranstaltung lehrveranstaltung);
    List<TeilnehmerListe> findAllByLehrveranstaltung(Lehrveranstaltung lehrveranstaltung);
    TeilnehmerListe findByLehrveranstaltung(long id);
}
