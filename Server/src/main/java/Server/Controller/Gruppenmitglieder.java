package Server.Controller;

import Server.Modell.Gruppenmitglied;
import Server.Modell.Lehrmaterial;
import Server.Modell.Lehrveranstaltung;
import Server.Modell.TeilnehmerListe;
import Server.Repository.ProjektgruppenRepository;
import Server.Services.GruppenmitgliedService;
import Server.Services.TeilnehmerListeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/gruppenmitglieder")
public class Gruppenmitglieder {

    private ProjektgruppenRepository projektgruppenRepository;
    private GruppenmitgliedService gruppenmitgliedService;

    public Gruppenmitglieder(ProjektgruppenRepository projektgruppenRepository,GruppenmitgliedService gruppenmitgliedService ) {
        this.gruppenmitgliedService=gruppenmitgliedService;
        this.projektgruppenRepository=projektgruppenRepository;
    }

    @GetMapping("/{projektgruppeId}")
    public List<Gruppenmitglied> getAlleMitglieder(@PathVariable long projektgruppeId){

        List<Gruppenmitglied> mitglied= gruppenmitgliedService.mitglied(projektgruppeId);


        return mitglied;
    }
}
