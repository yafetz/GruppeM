package Server.Repository;

import Server.Modell.Lehrveranstaltung;
import Server.Modell.Nutzer;
import Server.Modell.Student;
import Server.Modell.TeilnehmerListe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeilnehmerListeRepository extends JpaRepository<TeilnehmerListe,Long> {
    List<TeilnehmerListe> findAllByLehrveranstaltung(Lehrveranstaltung lehrveranstaltung);
    TeilnehmerListe findById(long id);
    List<TeilnehmerListe> findDistinctByLehrveranstaltung(Lehrveranstaltung lehrveranstaltung);
    List<TeilnehmerListe> getDistinctByLehrveranstaltung(Lehrveranstaltung lehrveranstaltung);
    List<TeilnehmerListe> findAllByNutzerId(Nutzer nutzer);
    @Query("SELECT student FROM Student student LEFT JOIN TeilnehmerListe teilnehmerliste ON teilnehmerliste.nutzerId = student.nutzerId AND teilnehmerliste.lehrveranstaltung.id = ?1 WHERE teilnehmerliste.nutzerId IS NULL ")
    List<Student> findAllStudentsWhoAreNotAlreadyInLehrveranstaltung(long id);
    @Query("SELECT student FROM Student student LEFT JOIN TeilnehmerListe teilnehmerliste ON teilnehmerliste.nutzerId = student.nutzerId AND teilnehmerliste.lehrveranstaltung.id = ?1 " +
            "JOIN Nutzer nutzer On nutzer.id = student.nutzerId.id WHERE teilnehmerliste.nutzerId IS NULL AND  nutzer.vorname LIKE %?2% OR nutzer.nachname LIKE %?2% ")
    List<Student> findAllStudentsByKeywordVornameUndNachname(long id,String keyword);
    @Query("SELECT student FROM Student student LEFT JOIN TeilnehmerListe teilnehmerliste ON teilnehmerliste.nutzerId = student.nutzerId AND teilnehmerliste.lehrveranstaltung.id = ?1 " +
            "WHERE teilnehmerliste.nutzerId IS NULL AND student.matrikelnummer = ?2 ")
    List<Student> findAllStudentsByKeywordMatrikelnummer(long id,int keyword);
    Boolean existsByLehrveranstaltungAndNutzerId(Lehrveranstaltung lehrveranstaltung, Nutzer nutzer);
    @Query("SELECT student FROM Student student INNER JOIN TeilnehmerListe teilnehmerliste ON teilnehmerliste.nutzerId = student.nutzerId AND teilnehmerliste.lehrveranstaltung.id = ?1 ")
    List<Student> getAllStudByLehrveranstaltungId (Long lehrveranstaltungId);

    @Query("SELECT COUNT(student) FROM Student student JOIN TeilnehmerListe teilnehmerliste ON teilnehmerliste.nutzerId = student.nutzerId AND teilnehmerliste.lehrveranstaltung.id = ?1")
   int getAllStudents(long id);


    TeilnehmerListe findTeilnehmerListeByLehrveranstaltungAndNutzerId(Lehrveranstaltung lehrveranstaltung,Nutzer nutzer);

}

