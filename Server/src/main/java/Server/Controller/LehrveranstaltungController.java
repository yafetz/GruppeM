package Server.Controller;

import Server.Modell.Lehrender;
import Server.Modell.Lehrveranstaltung;
import Server.Modell.Nutzer;
import Server.Modell.TeilnehmerListe;
import Server.Repository.LehrenderRepository;
import Server.Repository.LehrveranstaltungRepository;
import Server.Repository.NutzerRepository;
import Server.Repository.TeilnehmerListeRepository;
import Server.Services.LehrveranstaltungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/lehrveranstaltung")
public class LehrveranstaltungController {
    private final LehrveranstaltungService lehrveranstaltungService;
    private final LehrveranstaltungRepository lehrveranstaltungRepository;
    private final TeilnehmerListeRepository teilnehmerListeRepository;
    private final NutzerRepository nutzerRepository;
    private final LehrenderRepository lehrenderRepository;
    private final LehrveranstaltungService lehrveranstaltungErstellenService;

    @Autowired
    public LehrveranstaltungController(LehrveranstaltungService lehrveranstaltungService, LehrveranstaltungRepository lehrveranstaltungRepository, TeilnehmerListeRepository teilnehmerListeRepository, NutzerRepository nutzerRepository, LehrenderRepository lehrenderRepository, LehrveranstaltungService lehrveranstaltungErstellenService) {
        this.lehrveranstaltungService = lehrveranstaltungService;
        this.lehrveranstaltungRepository = lehrveranstaltungRepository;
        this.teilnehmerListeRepository = teilnehmerListeRepository;
        this.nutzerRepository = nutzerRepository;
        this.lehrenderRepository = lehrenderRepository;
        this.lehrveranstaltungErstellenService = lehrveranstaltungErstellenService;
    }

    @PostMapping("/beitreten/{lehrveranstaltungsId}&{nutzer_id}")
    public Object beitreten(@PathVariable long lehrveranstaltungsId,@PathVariable long nutzer_id){

        return lehrveranstaltungService.beitreten(lehrveranstaltungsId, nutzer_id);
    }

    @GetMapping("/check/{lehrveranstaltungsId}&{nutzer_id}")
    public boolean isMember(@PathVariable long lehrveranstaltungsId,@PathVariable long nutzer_id){
        Lehrveranstaltung lehrveranstaltung = lehrveranstaltungRepository.findLehrveranstaltungById(lehrveranstaltungsId);
        Nutzer nutzer = nutzerRepository.findNutzerById(nutzer_id);
        if(teilnehmerListeRepository.existsByLehrveranstaltungAndNutzerId(lehrveranstaltung,nutzer)){
            return true;
        };

        return false;
    }
    @PostMapping("/create/lehrveranstaltung/{titel}&{lehrenderd}&{art}&{semester}")
    public void newLehrveranstaltung(@PathVariable String titel, @PathVariable Nutzer lehrenderd, @PathVariable String art, @PathVariable String semester){
        Lehrender lehrender = lehrenderRepository.findLehrenderByNutzerId(lehrenderd);
        long lehrenderId=  lehrender.getId();
        lehrveranstaltungErstellenService.createNewLehrveranstaltung(titel,lehrenderId,art,semester);
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

    @PostMapping("/csv")
    public ResponseEntity<String> CsvUpload(@RequestParam("files") List<MultipartFile> multipartFiles,
                                            @RequestParam("nutzerId") Long nutzerId) throws IOException {
        lehrveranstaltungErstellenService.extractData(multipartFiles,nutzerId);
        return new ResponseEntity<>("Servernachricht: CSV-Datei erfolgreich hochgeladen!", null, HttpStatus.OK);
    }

}


