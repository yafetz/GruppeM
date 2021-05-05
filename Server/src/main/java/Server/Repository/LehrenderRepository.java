package Server.Repository;

import Server.Modell.Lehrender;
import Server.Modell.Nutzer;
import Server.Modell.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LehrenderRepository extends JpaRepository<Lehrender,Long> {
    Lehrender findLehrenderByNutzerId(Nutzer nutzer);
}
