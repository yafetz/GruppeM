package Server.Controller;

import Server.Modell.Lehrender;
import Server.Modell.Nutzer;
import Server.Repository.LehrenderRepository;
import Server.Repository.LehrveranstaltungRepository;
import Server.Services.LehrveranstaltungErstellenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/create")
public class LehrveranstaltungErstellenController {
    private final LehrveranstaltungErstellenService lehrveranstaltungErstellenService;
    private final LehrveranstaltungRepository lvRepo;
    private final LehrenderRepository lehrenderRepository;

    @Autowired
    public LehrveranstaltungErstellenController(LehrveranstaltungErstellenService lehrveranstaltungErstellenService, LehrveranstaltungRepository lvRepo, LehrenderRepository lehrenderRepository) {
        this.lehrveranstaltungErstellenService = lehrveranstaltungErstellenService;
        this.lvRepo = lvRepo;
        this.lehrenderRepository = lehrenderRepository;
    }


    @PostMapping("/lehrveranstaltung/{titel}&{lehrenderd}&{art}&{semester}")
    public void newLehrveranstaltung(@PathVariable String titel, @PathVariable Nutzer lehrenderd, @PathVariable String art, @PathVariable String semester){
       Lehrender lehrender = lehrenderRepository.findLehrenderByNutzerId(lehrenderd);
       long lehrenderId=  lehrender.getId();
        lehrveranstaltungErstellenService.createNewLehrveranstaltung(titel,lehrenderId,art,semester);
    }

}
