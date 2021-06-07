package Server.Repository;

import Server.Modell.Gruppenmitglied;
import Server.Modell.Projektgruppe;
import Server.Modell.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GruppenmitgliedRepository extends JpaRepository<Gruppenmitglied, Long> {

    List<Gruppenmitglied> findAllByProjektgruppe(Projektgruppe projektgruppe);
    List<Gruppenmitglied> findAllByStudentId(Student student);
}
