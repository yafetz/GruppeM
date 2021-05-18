package Server.Controller;

import Server.Modell.Lehrveranstaltung;
import Server.Modell.Nutzer;
import Server.Modell.TeilnehmerListe;
import Server.Repository.LehrveranstaltungRepository;
import Server.Repository.NutzerRepository;
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
    private final NutzerRepository nutzerRepository;

    @Autowired
    public LehrveranstaltungController(LehrveranstaltungRepository lehrveranstaltungRepository, TeilnehmerListeRepository teilnehmerListeRepository, NutzerRepository nutzerRepository) {
        this.lehrveranstaltungRepository = lehrveranstaltungRepository;
        this.teilnehmerListeRepository = teilnehmerListeRepository;
        this.nutzerRepository = nutzerRepository;
    }

    @GetMapping("/all")
    public List<Lehrveranstaltung> getAllLehrveranstaltungen() {
        return lehrveranstaltungRepository.findAll();
    }

    @GetMapping("/meine/nutzerId={nutzerId}")
    public List<TeilnehmerListe> getMeineLehrveranstaltungen(@PathVariable Long nutzerId) {
        Nutzer nutzer = nutzerRepository.findNutzerById(nutzerId);
        return  teilnehmerListeRepository.findAllByNutzerId(nutzer);
    }

    @GetMapping("/{lehrveranstaltungId}")
    public Optional<Lehrveranstaltung> getLehrveranstaltungWithId(@PathVariable Long lehrveranstaltungId) {
        return lehrveranstaltungRepository.findById(lehrveranstaltungId);
    }
}


