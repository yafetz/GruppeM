package Server.Repository;

import Server.Modell.Nutzer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NutzerRepository extends JpaRepository<Nutzer,Long> {
    Nutzer findNutzerById(Long Id);
    Nutzer findNutzerByEmail(String email);

}
