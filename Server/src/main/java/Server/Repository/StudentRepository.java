package Server.Repository;

import Server.Modell.Nutzer;
import Server.Modell.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StudentRepository extends JpaRepository<Student,Long> {
    Student findStudentByMatrikelnummer(Integer matrikelnummer);
    Student findStudentByNutzerId(Nutzer nutzer);
}
