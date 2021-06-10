package Server.Controller;

import Server.Modell.*;
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
    private final GruppenmitgliedRepository gruppenmitgliedRepository;

    @Autowired
    public ProjektgruppenController(ProjektgruppenRepository projektgruppenRepository, ProjektgruppenService projektgruppenService, LehrveranstaltungRepository lehrveranstaltungRepository, LehrenderRepository lehrenderRepository, StudentRepository studentRepository, NutzerRepository nutzerRepository, GruppenmitgliedRepository gruppenmitgliedRepository) {
        this.projektgruppenRepository = projektgruppenRepository;
        this.projektgruppenService = projektgruppenService;
        this.lehrveranstaltungRepository = lehrveranstaltungRepository;
        this.lehrenderRepository = lehrenderRepository;
        this.studentRepository = studentRepository;
        this.nutzerRepository = nutzerRepository;
        this.gruppenmitgliedRepository = gruppenmitgliedRepository;
    }

    @PostMapping("/neu")
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

    @GetMapping("/checkMember/{pgID}&{studentID}")
    public boolean istGruppenmitglied (@PathVariable Long pgID, @PathVariable Long studentID) {
        Student student = studentRepository.findStudentById(studentID);
        Projektgruppe projektgruppe = projektgruppenRepository.findProjektgruppeById(pgID);
        if (gruppenmitgliedRepository.existsByProjektgruppeAndStudent(projektgruppe, student) ) {
            return true;
        } else {
            return false;
        }
    }

    @GetMapping("/{projektgruppeId}")
    public Projektgruppe getProjektgruppeWithId(@PathVariable Long projektgruppeId) {
        return projektgruppenRepository.findProjektgruppeById(projektgruppeId);
    }

    @PostMapping("/newMember")
    public ResponseEntity<String> addGruppenmitglied(@RequestParam("projektgruppeId") long projektgruppeId, @RequestParam("studentId") long studentId) {
        Student student = studentRepository.findStudentById(studentId);
        Projektgruppe projektgruppe = projektgruppenRepository.findProjektgruppeById(projektgruppeId);
        Gruppenmitglied neu = new Gruppenmitglied(student, projektgruppe);
        gruppenmitgliedRepository.save(neu);
        return new ResponseEntity<>("Erfolgreich der Projektgruppe " + projektgruppe.getTitel() + " beigetreten!", null, HttpStatus.OK);
    }

    @PostMapping("/suchen")
    public List<Projektgruppe> getAllByLehrveranstaltungAndKeyword(@RequestParam("lvID") Long lvID, @RequestParam("titel") String titel) {
        return projektgruppenRepository.getAllProjektgruppeByLehrveranstaltungAndKeyword(lvID, titel);
    }
}
