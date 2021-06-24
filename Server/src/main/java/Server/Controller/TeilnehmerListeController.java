package Server.Controller;


import Server.Modell.Lehrveranstaltung;
import Server.Modell.Nutzer;
import Server.Modell.Student;
import Server.Modell.TeilnehmerListe;
import Server.Repository.*;
import Server.Services.TeilnehmerListeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        return teilnehmer;
    }

    @PostMapping("/suchen")
    public List<Student> getAllStudentenByKeyword(@RequestParam("lehrveranstaltungsId") long lehrveranstaltungsId, @RequestParam("keyword") String keyword){
        if(keyword.matches("[0-9]+")){
            return teilnehmerListeRepository.findAllStudentsByKeywordMatrikelnummer(lehrveranstaltungsId,Integer.valueOf(keyword));
        }else {
            return teilnehmerListeRepository.findAllStudentsByKeywordVornameUndNachname(lehrveranstaltungsId, keyword);
        }
    }

    @GetMapping("/studentenliste/{lehrveranstaltungsId}")
    public List<Student> getAllStudenten(@PathVariable long lehrveranstaltungsId) {
        return teilnehmerListeRepository.findAllStudentsWhoAreNotAlreadyInLehrveranstaltung(lehrveranstaltungsId);
    }

    @PostMapping("/add")
    public String add_teilnehmer(@RequestParam("studentId") Long studentId,@RequestParam("lehrveranstaltungId") Long lehrveranstaltungId){
        TeilnehmerListe tl = new TeilnehmerListe();
        tl.setLehrveranstaltung(lehrveranstaltungRepository.findLehrveranstaltungById(lehrveranstaltungId));
        tl.setNutzerId(studentRepository.findStudentById(studentId).getNutzerId());
        teilnehmerListeRepository.save(tl);
        return "OK";
    }

    @GetMapping("/teilnehmerId={teilnehmerId}")
    public Object getNutzerWithTeilnehmerId(@PathVariable long teilnehmerId) {
        Nutzer nutzer = nutzerRepository.findNutzerById(teilnehmerId);
        if (nutzer.getRolle().equals("Lehrender")) {
            return lehrenderRepository.findLehrenderByNutzerId(nutzer);
        }
        if (nutzer.getRolle().equals("Student")) {
            return studentRepository.findStudentByNutzerId(nutzer);
        }
        return null;
    }


@GetMapping("/allelehrveranstaltungen/{nutzerId}")
        public List<TeilnehmerListe> alleLehrveranstaltungen(@PathVariable long nutzerId){
            return teilnehmerListeRepository.findAllByNutzerId(nutzerRepository.findNutzerById(nutzerId));
        }

}
