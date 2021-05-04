package Server.Repository;

import Server.Modell.Nutzer;
import Server.Modell.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NutzerRepository extends JpaRepository<Nutzer,Long> {
    Nutzer findNutzerById(Long Id);
    Nutzer findNutzerByEmail(String email);
}
