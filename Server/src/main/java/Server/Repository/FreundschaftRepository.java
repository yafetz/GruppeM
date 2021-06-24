package Server.Repository;

import Server.Modell.Freundschaft;
import Server.Modell.Lehrveranstaltung;
import Server.Modell.Nutzer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FreundschaftRepository extends JpaRepository<Freundschaft,Long> {

    Freundschaft findAllById(long id);

    @Query("SELECT freundschaft FROM Freundschaft freundschaft WHERE freundschaft.angefragter_nutzer = ?1 AND freundschaft.status=false")
    List<Freundschaft> getAllFreundschaftsAnfragen(Nutzer nutzer);
    @Query("SELECT freundschaft FROM Freundschaft freundschaft WHERE freundschaft.angefragter_nutzer=?1 AND freundschaft.anfragender_nutzer=?2")
    Freundschaft getFreundschaft(Nutzer nutzer, Nutzer nutzer2);
    @Query("SELECT freundschaft FROM Freundschaft freundschaft WHERE freundschaft.anfragender_nutzer=?1 OR freundschaft.angefragter_nutzer=?1 AND freundschaft.status=true ")
    List<Freundschaft> getFreundschaften(Nutzer nutzer);
    @Query("SELECT freundschaft FROM Freundschaft freundschaft WHERE freundschaft.angefragter_nutzer=?1 AND freundschaft.anfragender_nutzer=?2 OR freundschaft.anfragender_nutzer=?1 AND  freundschaft.angefragter_nutzer=?2 ")
    Freundschaft getStatus(Nutzer nutzer1, Nutzer nutzer2);




}
