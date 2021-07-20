package Server.Repository;

import Server.Modell.Lehrveranstaltung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LehrveranstaltungRepository extends JpaRepository<Lehrveranstaltung,Long> {
    Lehrveranstaltung findAllById(long id);
    Lehrveranstaltung findLehrveranstaltungById(long id);
    @Query("SELECT count(veranstaltung) > 0 from Lehrveranstaltung veranstaltung where titel LIKE ?1 AND art LIKE ?2 AND semester LIKE ?3")
    boolean existsIfTitelAndArtAndSemester(String titel, String art,String semester);
    @Query("SELECT lehrveranstaltung FROM Lehrveranstaltung lehrveranstaltung WHERE lehrveranstaltung.titel LIKE %?1%")
    List<Lehrveranstaltung> getAllLehrveranstaltungByKeyword(String keyword);


    @Query("SELECT lehrveranstaltung.id FROM Lehrveranstaltung lehrveranstaltung WHERE lehrveranstaltung.semester LIKE %?1%")
    List<Long> getAllLehrveranstaltungBySemester(String keyword);

}
