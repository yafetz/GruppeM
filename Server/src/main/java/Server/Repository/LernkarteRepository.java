package Server.Repository;

import Server.Modell.Lernkarte;
import Server.Modell.Lernkartenset;
import Server.Modell.Nutzer;
import Server.Modell.Projektgruppe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LernkarteRepository extends CrudRepository<Lernkarte, Long> {

//    List<Lernkarte> findByProjektgruppe(Projektgruppe projektgruppe);
    List<Lernkarte> findByLernkartenset(Lernkartenset lernkartenset);
}
