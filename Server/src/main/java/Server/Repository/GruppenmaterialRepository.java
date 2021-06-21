package Server.Repository;

import Server.Modell.Gruppenmaterial;
import Server.Modell.Lehrmaterial;
import Server.Modell.Projektgruppe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GruppenmaterialRepository extends JpaRepository<Gruppenmaterial, Long> {

    List<Gruppenmaterial> findGruppenmaterialByProjektgruppe(Projektgruppe projektgruppe);
    List<Gruppenmaterial> findGruppenmaterialByProjektgruppeId(long id);
    Gruppenmaterial findGruppenmaterialById(long id);

}
