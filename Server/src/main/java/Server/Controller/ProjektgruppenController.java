package Server.Controller;

import Server.Modell.Lehrveranstaltung;
import Server.Modell.Nutzer;
import Server.Modell.Projektgruppe;
import Server.Repository.*;
import Server.Services.ProjektgruppenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projektgruppe")
public class ProjektgruppenController {
    private final ProjektgruppenRepository projektgruppenRepository;
    private final ProjektgruppenService projektgruppenService;
    private final LehrveranstaltungRepository lehrveranstaltungRepository;
    private final LehrenderRepository lehrenderRepository;
    private final StudentRepository studentRepository;
    private final NutzerRepository nutzerRepository;

    @Autowired
    public ProjektgruppenController(ProjektgruppenRepository projektgruppenRepository, ProjektgruppenService projektgruppenService, LehrveranstaltungRepository lehrveranstaltungRepository, LehrenderRepository lehrenderRepository, StudentRepository studentRepository, NutzerRepository nutzerRepository) {
        this.projektgruppenRepository = projektgruppenRepository;
        this.projektgruppenService = projektgruppenService;
        this.lehrveranstaltungRepository = lehrveranstaltungRepository;
        this.lehrenderRepository = lehrenderRepository;
        this.studentRepository = studentRepository;
        this.nutzerRepository = nutzerRepository;
    }

    @PostMapping("/new")
    public ResponseEntity<Boolean> projektgruppeErstellen(@RequestParam("titel") String titel, @RequestParam("nutzer") Long nutzerID, @RequestParam("lehrveranstaltung") Long lvID) {
        Nutzer nutzer = nutzerRepository.findNutzerById(nutzerID);
        Projektgruppe projektgruppe = projektgruppenRepository.findProjektgruppeByTitel(titel);
        Lehrveranstaltung lehrveranstaltung = lehrveranstaltungRepository.findLehrveranstaltungById(lvID);
        if (projektgruppe != null) {
            // Projektgruppe mit diesem Titel bereits vorhanden
            return new ResponseEntity<>(false, null, HttpStatus.BAD_REQUEST);
        } else {
            // Projektgruppe noch nicht vorhanden
            projektgruppenService.createProjektgruppe(lehrveranstaltung, titel, nutzer);
            return new ResponseEntity<>(true, null, HttpStatus.OK);
        }
    }

    @GetMapping("/lvId={lvID}")
    public List<Projektgruppe> getProjektgruppenOfLehrveranstaltung(@PathVariable Long lvID) {
        return projektgruppenRepository.findAllByLehrveranstaltungId(lvID);
    }
}
