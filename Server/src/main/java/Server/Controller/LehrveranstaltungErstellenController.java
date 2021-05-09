package Server.Controller;


import Server.Modell.Lehrender;
import Server.Modell.Lehrveranstaltung;
import Server.Repository.LehrenderRepository;
import Server.Repository.LehrveranstaltungRepository;
import Server.Services.LehrveranstaltungErstellenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/create")
public class LehrveranstaltungErstellenController {
    private final LehrveranstaltungErstellenService lehrveranstaltungErstellenService;
    LehrveranstaltungRepository lvRepo;
    LehrenderRepository lehrenderRepository;



    @Autowired
    public LehrveranstaltungErstellenController(LehrveranstaltungErstellenService lehrveranstaltungErstellenService, LehrveranstaltungRepository lvRepo, LehrenderRepository lehrenderRepository) {
        this.lehrveranstaltungErstellenService = lehrveranstaltungErstellenService;
        this.lvRepo = lvRepo;
        this.lehrenderRepository = lehrenderRepository;
    }



    @PostMapping("/lehrveranstaltung/{titel}&{lehrenderId}&{art}&{semester}")
    public void newLehrveranstaltung(@PathVariable String titel, @PathVariable long lehrenderId,@PathVariable String art, @PathVariable String semester){
        lehrveranstaltungErstellenService.createNewLehrveranstaltung(titel,lehrenderId,art,semester);
    }

}
