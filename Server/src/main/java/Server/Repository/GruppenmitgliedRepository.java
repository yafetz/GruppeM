package Server.Repository;

import Server.Modell.Gruppenmitglied;
import Server.Modell.Projektgruppe;
import Server.Modell.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GruppenmitgliedRepository extends JpaRepository<Gruppenmitglied, Long> {

    List<Gruppenmitglied> findAllByProjektgruppe(Projektgruppe projektgruppe);

    List<Gruppenmitglied> findAllByStudentId(Student student);
    Boolean existsByProjektgruppeAndStudent(Projektgruppe projektgruppe, Student student);
    @Query("SELECT student FROM Student student INNER JOIN Gruppenmitglied gruppenmitglied ON gruppenmitglied.student = student WHERE gruppenmitglied.projektgruppe.id = ?1 ")
    List<Student> findAllStudentsByProjektgruppe(long projektgruppenId);
    @Query("SELECT student FROM Student student LEFT JOIN Gruppenmitglied gruppenmitglied ON gruppenmitglied.student = student AND gruppenmitglied.projektgruppe.id = ?2 INNER JOIN "+
            "TeilnehmerListe teilnehmerliste ON teilnehmerliste.nutzerId = student.nutzerId AND teilnehmerliste.lehrveranstaltung.id = ?1 WHERE gruppenmitglied.student IS NULL ")
    List<Student> getAllStudWhoAreNotMitglied(long lvId, long projektgruppenId);
}
