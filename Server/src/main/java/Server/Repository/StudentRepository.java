package Server.Repository;

import Server.Modell.Nutzer;
import Server.Modell.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
    Student findStudentByMatrikelnummer(Integer matrikelnummer);
    Student findStudentByNutzerId(Nutzer nutzer);
    Student findTopByOrderByIdDesc();
    Student findStudentById(long id);

    @Query("SELECT t.nutzerId FROM TeilnehmerListe t WHERE t.lehrveranstaltung.id LIKE ?1 ")
    List<Nutzer> getAllIDsFromStudentsFromACourse(long lehrveranstaltungs_id);
}
