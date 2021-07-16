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

    @Autowired
    public LehrveranstaltungController(LehrveranstaltungService lehrveranstaltungService, LehrveranstaltungRepository lehrveranstaltungRepository, TeilnehmerListeRepository teilnehmerListeRepository, NutzerRepository nutzerRepository, LehrenderRepository lehrenderRepository, LehrveranstaltungService lehrveranstaltungErstellenService) {
        this.lehrveranstaltungService = lehrveranstaltungService;
        this.lehrveranstaltungRepository = lehrveranstaltungRepository;
        this.teilnehmerListeRepository = teilnehmerListeRepository;
        this.nutzerRepository = nutzerRepository;
        this.lehrenderRepository = lehrenderRepository;

    }

    @PostMapping("/beitreten/{lehrveranstaltungsId}&{nutzer_id}")
    public Object beitreten(@PathVariable long lehrveranstaltungsId,@PathVariable long nutzer_id){

        return lehrveranstaltungService.beitreten(lehrveranstaltungsId, nutzer_id);
    }

    @PostMapping("/suchen")
    public List<Lehrveranstaltung> getAllByKeyword(@RequestParam("titel") String titel){
        return lehrveranstaltungRepository.getAllLehrveranstaltungByKeyword(titel);
    }

    @GetMapping("/beitreten/check/{lehrveranstaltungsId}&{nutzer_id}")
    public boolean isMember(@PathVariable long lehrveranstaltungsId,@PathVariable long nutzer_id){
        Lehrveranstaltung lehrveranstaltung = lehrveranstaltungRepository.findLehrveranstaltungById(lehrveranstaltungsId);
        Nutzer nutzer = nutzerRepository.findNutzerById(nutzer_id);
        if(teilnehmerListeRepository.existsByLehrveranstaltungAndNutzerId(lehrveranstaltung,nutzer)){
            return true;
        };

        return false;
    }
    @PostMapping("/create/lehrveranstaltung/")
    public void newLehrveranstaltung(@RequestParam("titel") String titel, @RequestParam("lehrenderd") Nutzer lehrenderd, @RequestParam("art") String art, @RequestParam("semester") String semester){
        Lehrender lehrender = lehrenderRepository.findLehrenderByNutzerId(lehrenderd);
        long lehrenderId=  lehrender.getId();
        lehrveranstaltungService.createNewLehrveranstaltung(titel,lehrenderId,art,semester);
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
        lehrveranstaltungService.extractData(multipartFiles,nutzerId);
        return new ResponseEntity<>("Servernachricht: CSV-Datei erfolgreich hochgeladen!", null, HttpStatus.OK);
    }

    @PostMapping("/update/lehrveranstaltung/")
    public void updateCourse(@RequestParam("titel") String titel, @RequestParam("lehrenderd") Nutzer lehrenderd, @RequestParam("art") String art, @RequestParam("semester") String semester,@RequestParam("id") long id){

        Lehrender lehrender = lehrenderRepository.findLehrenderByNutzerId(lehrenderd);
        long lehrenderId=  lehrender.getId();
        lehrveranstaltungService.updateLehrveranstaltung(titel,lehrenderId,art,semester, id, lehrenderd);
    }

}


