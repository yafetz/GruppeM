package Server.Controller;


import Server.Modell.Student;
import Server.Modell.TeilnehmerListe;
import Server.Repository.LehrenderRepository;
import Server.Repository.LehrveranstaltungRepository;
import Server.Repository.StudentRepository;
import Server.Repository.TeilnehmerListeRepository;
import Server.Services.TeilnehmerListeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/teilnehmer")
public class TeilnehmerListeController {
    StudentRepository studentRepository;
    LehrveranstaltungRepository lehrveranstaltungRepository;
    LehrenderRepository lehrenderRepository;
    TeilnehmerListeService teilnehmerListeService;
    @Autowired
    TeilnehmerListeRepository teilnehmerListeRepository;


    public TeilnehmerListeController(StudentRepository studentRepository, LehrveranstaltungRepository lehrveranstaltungRepository, LehrenderRepository lehrenderRepository, TeilnehmerListeService teilnehmerListeService) {
        this.studentRepository = studentRepository;
        this.lehrveranstaltungRepository = lehrveranstaltungRepository;
        this.lehrenderRepository = lehrenderRepository;
        this.teilnehmerListeService = teilnehmerListeService;

    }
    @GetMapping("/{lehrveranstaltungsId}")
    public List<TeilnehmerListe> alleTeilnehmer(@PathVariable long lehrveranstaltungsId){
        List<TeilnehmerListe> teilnehmer= teilnehmerListeService.teilnehmer(lehrveranstaltungsId);

        return teilnehmer;
    }



}
