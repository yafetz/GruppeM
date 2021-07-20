package Server.Repository;

import Server.Modell.Nutzer;
import Server.Modell.Thema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ThemenRepository extends JpaRepository<Thema,Long> {
    List<Thema> findAllByNutzer(Nutzer nutzer);
    Thema findThemaById(Long id);
}
