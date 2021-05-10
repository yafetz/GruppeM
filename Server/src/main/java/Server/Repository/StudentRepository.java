package Server.Repository;

import Server.Modell.Nutzer;
import Server.Modell.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student,Long> {
    Student findStudentByMatrikelnummer(Integer matrikelnummer);
    Student findStudentByNutzerId(Nutzer nutzer);
    Student findStudentById(long id);
}
