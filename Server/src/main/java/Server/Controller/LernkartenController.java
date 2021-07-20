package Server.Controller;

import Server.Modell.*;
import Server.Repository.LernkarteRepository;
import Server.Repository.LernkartensetRepository;
import Server.Repository.NutzerRepository;
import Server.Repository.ProjektgruppenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lernkarten")
public class LernkartenController {

    private final LernkarteRepository lernkarteRepository;
    private final LernkartensetRepository lernkartensetRepository;
    private final ProjektgruppenRepository projektgruppenRepository;
    private final NutzerRepository nutzerRepository;

    @Autowired
    public LernkartenController(LernkarteRepository lernkarteRepository, LernkartensetRepository lernkatensetRepository, ProjektgruppenRepository projektgruppenRepository, NutzerRepository nutzerRepository) {
        this.lernkarteRepository = lernkarteRepository;
        this.lernkartensetRepository = lernkatensetRepository;
        this.projektgruppenRepository = projektgruppenRepository;
        this.nutzerRepository = nutzerRepository;
    }

    @GetMapping("/getlernkarten/{lernkartenset_id}")
    public ResponseEntity<List<Lernkarte>> getLernKarten(@PathVariable long lernkartenset_id){

        List<Lernkarte> lernkarten = lernkarteRepository.findByLernkartenset(lernkartensetRepository.findLernkartensetById(lernkartenset_id));

//        lernkarten.forEach(lernkarte -> lernkarte.setProjektgruppe(null));

        return new ResponseEntity<>(lernkarten, null, HttpStatus.OK);
    }

    @PostMapping("/createLernkarte")
    public ResponseEntity<String> addLernkarte(
            @RequestParam("lernkartenset_id") long lernkartenset_id,
            @RequestParam("frage") String frage,
            @RequestParam("antwort") String antwort) {

//        Projektgruppe projektgruppe = projektgruppenRepository.findProjektgruppeById(projektgruppen_id);

//        if(projektgruppe == null) {
//            return new ResponseEntity<>("Fehler: Projektgruppe nicht gefunden", null, HttpStatus.BAD_REQUEST);
//        }

        Lernkarte lernkarte = new Lernkarte();
//        lernkarte.setProjektgruppe(projektgruppe);
        lernkarte.setLernkartenset(lernkartensetRepository.findLernkartensetById(lernkartenset_id));
        lernkarte.setFrage(frage);
        lernkarte.setAntwort(antwort);
        lernkarteRepository.save(lernkarte);

        return new ResponseEntity<>("Lernkarte erfolgreich erstellt", null, HttpStatus.OK);
    }

    @PostMapping("/createLernkartenset")
    public ResponseEntity<String> addLernkartenset(@RequestParam("bezeichnung") String bezeichnung, @RequestParam("erstellerId") long erstellerId,
                                                   @RequestParam("projektgruppenId") long projektgruppenId) {
        Projektgruppe projektgruppe = projektgruppenRepository.findProjektgruppeById(projektgruppenId);
        if(lernkartensetRepository.findLernkartensetByBezeichnungAndProjektgruppe(bezeichnung, projektgruppe) != null) {
            return new ResponseEntity<>("Lernkartenset mit dieser Bezeichnung bereits vorhanden", null, HttpStatus.BAD_REQUEST);
        }
        Lernkartenset set = new Lernkartenset(bezeichnung, nutzerRepository.findNutzerById(erstellerId), projektgruppe, false);
        lernkartensetRepository.save(set);

        return new ResponseEntity<>("Lernkartenset erfolgreich erstellt", null, HttpStatus.OK);
    }

    @GetMapping("/alleLernkartensets/{projektgruppenId}")
    public List<Lernkartenset> getAlleLernkartensetsInProjektgruppe(@PathVariable("projektgruppenId") long projektgruppenId) {
        return lernkartensetRepository.getAllByProjektgruppe(projektgruppenRepository.findProjektgruppeById(projektgruppenId));
    }

    @GetMapping("/geteilteLernkartensets/{projektgruppenId}")
    public List<Lernkartenset> getGeteilteLernkartensetsInProjektgruppe(@PathVariable("projektgruppenId") long projektgruppenId) {
        return lernkartensetRepository.getAllByProjektgruppeAndIstGeteilt(projektgruppenRepository.findProjektgruppeById(projektgruppenId), true);
    }

    @GetMapping("/eigeneLernkartensets/{projektgruppenId}&{nutzerId}")
    public List<Lernkartenset> getEigeneLernkartensetsInProjektgruppe(@PathVariable("projektgruppenId") long projektgruppenId, @PathVariable("nutzerId") long nutzerId) {
        return lernkartensetRepository.getAllByProjektgruppeAndErsteller(projektgruppenRepository.findProjektgruppeById(projektgruppenId), nutzerRepository.findNutzerById(nutzerId));
    }

    @GetMapping("/ungeteilteLernkartensets/{projektgruppenId}&{nutzerId}")
    public List<Lernkartenset> getUngeteilteLernkartensetsInProjektgruppe(@PathVariable("projektgruppenId") long projektgruppenId, @PathVariable("nutzerId") long nutzerId) {
        return lernkartensetRepository.getAllByProjektgruppeAndErstellerAndIstGeteilt(projektgruppenRepository.findProjektgruppeById(projektgruppenId),
                nutzerRepository.findNutzerById(nutzerId), false);
    }

    @PostMapping("/teileLernkartensets")
    public String teileLernkartensets(@RequestParam("projektgruppenId") long projektgruppenId, @RequestParam("lernkartensetId") List<Long> lernkartensetId) {
        Projektgruppe projektgruppe = projektgruppenRepository.findProjektgruppeById(projektgruppenId);
        for (int i = 0; i < lernkartensetId.size(); i++) {
            Lernkartenset lernkartenset = lernkartensetRepository.findLernkartensetById(lernkartensetId.get(i));
            lernkartenset.setIstGeteilt(true);
            lernkartensetRepository.save(lernkartenset);
        }
        return "true";
    }
}
