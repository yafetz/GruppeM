package Server.Controller;

import Server.Modell.*;
import Server.Repository.*;
import Server.Services.TeilnehmerListeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/kalender")
public class KalenderController {

    private KalenderRepository kalenderRepository;
    private NutzerRepository nutzerRepository;
    private LehrenderRepository lehrenderRepository;
    private StudentRepository studentRepository;
    private LehrveranstaltungRepository lehrveranstaltungRepository;
    private final TeilnehmerListeService teilnehmerListeService;

    @Autowired
    public KalenderController(KalenderRepository kalenderRepository, NutzerRepository nutzerRepository, LehrenderRepository lehrenderRepository, StudentRepository studentRepository, KalenderRepository kalenderRepository1, NutzerRepository nutzerRepository1, LehrenderRepository lehrenderRepository1, StudentRepository studentRepository1, LehrveranstaltungRepository lehrveranstaltungRepository, TeilnehmerListeService teilnehmerListeService){
        this.kalenderRepository = kalenderRepository1;
        this.nutzerRepository = nutzerRepository1;
        this.lehrenderRepository = lehrenderRepository1;
        this.studentRepository = studentRepository1;
        this.lehrveranstaltungRepository = lehrveranstaltungRepository;
        this.teilnehmerListeService = teilnehmerListeService;
    }

    @GetMapping("alleTermine/{nutzerId}")
    public List<Termin> alleTermine(@PathVariable("nutzerId") long nutzerId){
        return kalenderRepository.findAllByNutzerId(nutzerRepository.findNutzerById(nutzerId));
    }

    @PostMapping("/neuerTermin")
    public String neuerTermin(@RequestParam("titel") String titel,
                              @RequestParam("von") String vonDateTime,
                              @RequestParam("bis") String bisDateTime,
                              @RequestParam("lehrveranstaltungsId") long lvId,
                              @RequestParam("reminderValue") int reminderValue,
                              @RequestParam("reminderArt") String reminderArt,
                              @RequestParam("reminderShow") String reminderShow,
                              @RequestParam("nutzer") long nutzerId){
        Nutzer nutzer = nutzerRepository.findNutzerById(nutzerId);
        Lehrender lehrender = lehrenderRepository.findLehrenderByNutzerId(nutzer);
            //Füge Termin nur für sich selber hinzu
            Termin termin = new Termin();
            termin.setTitel(titel);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            termin.setVon(LocalDateTime.parse(vonDateTime, formatter));
            termin.setBis(LocalDateTime.parse(bisDateTime, formatter));
            termin.setLehrveranstaltung(lehrveranstaltungRepository.findLehrveranstaltungById(lvId));
            termin.setReminderValue(reminderValue);
            termin.setReminderArt(reminderArt);
            termin.setReminderShow(reminderShow);
            termin.setNutzerId(nutzer);
            kalenderRepository.save(termin);
            if(lehrender != null){
                List<TeilnehmerListe> teilnehmer= teilnehmerListeService.teilnehmer(lvId);
                for(int i = 0; i < teilnehmer.size(); i++){
                    if(!teilnehmer.get(i).getNutzerId().getId().equals(nutzer.getId())) {
                        Termin termin1 = new Termin();
                        termin1.setTitel(titel);
                        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        termin1.setVon(LocalDateTime.parse(vonDateTime, formatter1));
                        termin1.setBis(LocalDateTime.parse(bisDateTime, formatter1));
                        termin1.setLehrveranstaltung(lehrveranstaltungRepository.findLehrveranstaltungById(lvId));
                        termin1.setReminderValue(reminderValue);
                        termin1.setReminderArt(reminderArt);
                        termin1.setReminderShow(reminderShow);
                        termin1.setNutzerId(teilnehmer.get(i).getNutzerId());
                        kalenderRepository.save(termin1);
                    }
                }
            }
        return "OK";

    }

    @PostMapping("/Terminbearbeiten")
    public String TerminBearbeiten(
                              @RequestParam("id") long id,
                              @RequestParam("titel") String titel,
                              @RequestParam("von") String vonDateTime,
                              @RequestParam("bis") String bisDateTime,
                              @RequestParam("lehrveranstaltungsId") long lvId,
                              @RequestParam("reminderValue") int reminderValue,
                              @RequestParam("reminderArt") String reminderArt,
                              @RequestParam("reminderShow") String reminderShow,
                              @RequestParam("nutzer") long nutzerId){
        Nutzer nutzer = nutzerRepository.findNutzerById(nutzerId);
        //Füge Termin nur für sich selber hinzu
        Termin termin = kalenderRepository.findTerminById(id);
        termin.setTitel(titel);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        termin.setVon(LocalDateTime.parse(vonDateTime, formatter));
        termin.setBis(LocalDateTime.parse(bisDateTime, formatter));
        termin.setLehrveranstaltung(lehrveranstaltungRepository.findLehrveranstaltungById(lvId));
        termin.setReminderValue(reminderValue);
        termin.setReminderArt(reminderArt);
        termin.setReminderShow(reminderShow);
        termin.setNutzerId(nutzer);
        kalenderRepository.save(termin);
        return "OK";

    }

}
