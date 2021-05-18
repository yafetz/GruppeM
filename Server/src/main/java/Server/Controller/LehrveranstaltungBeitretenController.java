package Server.Controller;

import Server.Modell.Lehrveranstaltung;
import Server.Modell.Nutzer;
import Server.Repository.LehrveranstaltungRepository;
import Server.Repository.NutzerRepository;
import Server.Repository.TeilnehmerListeRepository;
import Server.Services.LehrveranstaltungBeitretenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/beitreten/")
public class LehrveranstaltungBeitretenController {
    @Autowired
    LehrveranstaltungBeitretenService lehrveranstaltungBeitretenService;
    @Autowired
    TeilnehmerListeRepository teilnehmerListeRepository;
    @Autowired
    LehrveranstaltungRepository lehrveranstaltungRepository;
    @Autowired
    NutzerRepository nutzerRepository;

   /*
    @Autowired
    public LehrveranstaltungBeitretenController(LehrveranstaltungBeitretenService lehrveranstaltungBeitretenService) {
        this.lehrveranstaltungBeitretenService = lehrveranstaltungBeitretenService;
    }
*/
    @PostMapping("{lehrveranstaltungsId}&{nutzer_id}")
    public Object beitreten(@PathVariable long lehrveranstaltungsId,@PathVariable long nutzer_id){

        return lehrveranstaltungBeitretenService.beitreten(lehrveranstaltungsId, nutzer_id);
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
}
