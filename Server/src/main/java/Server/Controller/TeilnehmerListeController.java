package Server.Controller;


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
    private final StudentRepository studentRepository;
    private final LehrveranstaltungRepository lehrveranstaltungRepository;
    private final NutzerRepository nutzerRepository;
    private final LehrenderRepository lehrenderRepository;
    private final TeilnehmerListeService teilnehmerListeService;
    private final TeilnehmerListeRepository teilnehmerListeRepository;

    @Autowired
    public TeilnehmerListeController(StudentRepository studentRepository, LehrveranstaltungRepository lehrveranstaltungRepository, NutzerRepository nutzerRepository, LehrenderRepository lehrenderRepository, TeilnehmerListeService teilnehmerListeService, TeilnehmerListeRepository teilnehmerListeRepository) {
        this.studentRepository = studentRepository;
        this.lehrveranstaltungRepository = lehrveranstaltungRepository;
        this.lehrenderRepository = lehrenderRepository;
        this.teilnehmerListeService = teilnehmerListeService;
        this.nutzerRepository= nutzerRepository;
        this.teilnehmerListeRepository = teilnehmerListeRepository;
    }

    @GetMapping("/{lehrveranstaltungsId}")
    public List<TeilnehmerListe> getAlleTeilnehmer(@PathVariable long lehrveranstaltungsId){
        List<TeilnehmerListe> teilnehmer= teilnehmerListeService.teilnehmer(lehrveranstaltungsId);
        System.out.println("alles gut"+ lehrveranstaltungsId);

        return teilnehmer;
    }





}
