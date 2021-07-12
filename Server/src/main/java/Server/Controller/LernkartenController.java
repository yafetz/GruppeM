package Server.Controller;

import Server.Modell.*;
import Server.Repository.LernkarteRepository;
import Server.Repository.ProjektgruppenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lernkarten")
public class LernkartenController {

    @Autowired
    LernkarteRepository lernkarteRepository;

    @Autowired
    ProjektgruppenRepository projektgruppenRepository;

    @GetMapping("/getlernkarten/{projektgruppen_id}")
    public ResponseEntity<List<Lernkarte>> getLernKarten(@PathVariable long projektgruppen_id){

        List<Lernkarte> lernkarten = lernkarteRepository.findByProjektgruppe(projektgruppenRepository.findProjektgruppeById(projektgruppen_id));

        lernkarten.forEach(lernkarte -> lernkarte.setProjektgruppe(null));

        return new ResponseEntity<>(lernkarten, null, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<String> addGruppenmitglied(
            @RequestParam("projektgruppen_id") long projektgruppen_id,
            @RequestParam("frage") String frage,
            @RequestParam("antwort") String antwort) {

        Projektgruppe projektgruppe = projektgruppenRepository.findProjektgruppeById(projektgruppen_id);

        if(projektgruppe == null) {
            return new ResponseEntity<>("Fehler: Projektgruppe nicht gefunden", null, HttpStatus.BAD_REQUEST);
        }

        Lernkarte lernkarte = new Lernkarte();
        lernkarte.setProjektgruppe(projektgruppe);
        lernkarte.setFrage(frage);
        lernkarte.setAntwort(antwort);
        lernkarteRepository.save(lernkarte);

        return new ResponseEntity<>("Lernkarte erfolgreich erstellt", null, HttpStatus.OK);
    }
}
