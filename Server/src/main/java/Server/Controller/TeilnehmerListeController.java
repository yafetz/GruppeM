package Server.Controller;


import Server.Modell.Lehrveranstaltung;
import Server.Modell.Nutzer;
import Server.Modell.Student;
import Server.Modell.TeilnehmerListe;
import Server.Repository.*;
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
    NutzerRepository nutzerRepository;
    LehrenderRepository lehrenderRepository;
    TeilnehmerListeService teilnehmerListeService;
    @Autowired
    TeilnehmerListeRepository teilnehmerListeRepository;


    public TeilnehmerListeController(StudentRepository studentRepository, LehrveranstaltungRepository lehrveranstaltungRepository,NutzerRepository nutzerRepository ,LehrenderRepository lehrenderRepository, TeilnehmerListeService teilnehmerListeService) {
        this.studentRepository = studentRepository;
        this.lehrveranstaltungRepository = lehrveranstaltungRepository;
        this.lehrenderRepository = lehrenderRepository;
        this.teilnehmerListeService = teilnehmerListeService;
        this.nutzerRepository= nutzerRepository;

    }
    @GetMapping("/{lehrveranstaltungsId}")
    public List<TeilnehmerListe> getAlleTeilnehmer(@PathVariable long lehrveranstaltungsId){
        List<TeilnehmerListe> teilnehmer= teilnehmerListeService.teilnehmer(lehrveranstaltungsId);

        return teilnehmer;
    }

    /*@GetMapping("/{lehrveranstaltungsId/{nutzerId}")
    public boolean isNutzer(@PathVariable long lehrveranstaltungsId, @PathVariable long nutzerId){
        Nutzer nutzer = nutzerRepository.findNutzerById(nutzerId);
        Lehrveranstaltung lehrveranstaltung = lehrveranstaltungRepository.findLehrveranstaltungById(lehrveranstaltungsId);
        return teilnehmerListeRepository.findAllByLehrveranstaltungAndIdExists(lehrveranstaltung, nutzerId);

    }*/

}
