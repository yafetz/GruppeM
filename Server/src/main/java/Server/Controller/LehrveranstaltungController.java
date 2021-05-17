package Server.Controller;

import Server.Modell.Lehrveranstaltung;
import Server.Modell.Nutzer;
import Server.Modell.Student;
import Server.Modell.TeilnehmerListe;
import Server.Repository.LehrveranstaltungRepository;
import Server.Repository.NutzerRepository;
import Server.Repository.StudentRepository;
import Server.Repository.TeilnehmerListeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/lehrveranstaltung")
public class LehrveranstaltungController {

    private final LehrveranstaltungRepository lehrveranstaltungRepository;
    private final TeilnehmerListeRepository teilnehmerListeRepository;
    private final StudentRepository studentRepository;
    private final NutzerRepository nutzerRepository;

    @Autowired
    public LehrveranstaltungController(LehrveranstaltungRepository lehrveranstaltungRepository, TeilnehmerListeRepository teilnehmerListeRepository, StudentRepository studentRepository, NutzerRepository nutzerRepository) {
        this.lehrveranstaltungRepository = lehrveranstaltungRepository;
        this.teilnehmerListeRepository = teilnehmerListeRepository;
        this.studentRepository = studentRepository;
        this.nutzerRepository = nutzerRepository;
    }

    @GetMapping("/all")
    public List<Lehrveranstaltung> getAllLehrveranstaltungen() {
        return lehrveranstaltungRepository.findAll();
    }

    @GetMapping("/meine/nutzerId={nutzerId}")
    public List<TeilnehmerListe> getMeineLehrveranstaltungen(@PathVariable Long nutzerId) {
        Student student = studentRepository.findStudentById(nutzerId);
        // Neu hinzugefügt nach Änderung der Teilnehmerliste Tabelle
        Nutzer nutzer = nutzerRepository.findNutzerById(nutzerId);
        // Alter return Statement
        //return teilnehmerListeRepository.findAllByStudentId(student);

        // Neues return Statement
        return  teilnehmerListeRepository.findAllByNutzerId(nutzer);
    }

    @GetMapping("/{lehrveranstaltungId}")
    public Optional<Lehrveranstaltung> getLehrveranstaltungWithId(@PathVariable Long lehrveranstaltungId) {
        return lehrveranstaltungRepository.findById(lehrveranstaltungId);
    }
}


