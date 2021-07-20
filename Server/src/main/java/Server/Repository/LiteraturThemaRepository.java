package Server.Repository;

import Server.Modell.Literatur;
import Server.Modell.LiteraturThema;
import Server.Modell.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LiteraturThemaRepository extends JpaRepository<LiteraturThema,Long> {
    @Query("SELECT literatur FROM Literatur literatur LEFT JOIN LiteraturThema literaturThema ON literaturThema.literatur = literatur AND literaturThema.thema.id= ?1 WHERE literaturThema.literatur IS NULL ")
    List<Literatur> findAllLiteraturWhichAreNotSelected(long thema_id);

    @Query("SELECT literatur FROM Literatur literatur JOIN LiteraturThema literaturThema ON literaturThema.literatur = literatur AND literaturThema.thema.id= ?1")
    List<Literatur> findAllLiteraturByThema(long thema_id);
}
