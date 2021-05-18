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
   // TeilnehmerListe findAllByLehrveranstaltung(Lehrveranstaltung lehrveranstaltung);
//    TeilnehmerListe getAllByLehrveranstaltung(Lehrveranstaltung lehrveranstaltung);
    List<TeilnehmerListe> findAllByLehrveranstaltung(Lehrveranstaltung lehrveranstaltung);
  //  TeilnehmerListe findByLehrveranstaltung(long id);
    //List<Student> getAllById(Long id);
    //List<TeilnehmerListe> findAllByStudentId(Student student);
    List<TeilnehmerListe> findAllByNutzerId(Nutzer nutzer);
    //Boolean findAllByLehrveranstaltungAndIdExists(Lehrveranstaltung lehrveranstaltung, long id);
    Boolean existsByLehrveranstaltungAndNutzerId(Lehrveranstaltung lehrveranstaltung, Nutzer nutzer);
}

