package Server.Repository;

import Server.Modell.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ErgebnisVersendetRepository extends JpaRepository<ErgebnisVersendet, Long> {
    @Query("SELECT ev FROM ErgebnisVersendet ev Where ev.nutzer.id = ?1 AND ev.lehrveranstaltung.id = ?2 AND ev.semester = ?3 ")
    List<ErgebnisVersendet> findAllNutzerBySemesterCoursePassed(long nutzer, long lehrveranstaltung, String semester);

}
