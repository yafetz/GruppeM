package Server.Controller;

import Server.Modell.Lehrender;
import Server.Modell.Nutzer;
import Server.Modell.Student;
import Server.Modell.Termin;
import Server.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/kalender")
public class KalenderController {

    private KalenderRepository kalenderRepository;
    private NutzerRepository nutzerRepository;
    private LehrenderRepository lehrenderRepository;
    private StudentRepository studentRepository;
    private LehrveranstaltungRepository lehrveranstaltungRepository;

    @Autowired
    public KalenderController(KalenderRepository kalenderRepository, NutzerRepository nutzerRepository, LehrenderRepository lehrenderRepository, StudentRepository studentRepository, KalenderRepository kalenderRepository1, NutzerRepository nutzerRepository1, LehrenderRepository lehrenderRepository1, StudentRepository studentRepository1, LehrveranstaltungRepository lehrveranstaltungRepository){
        this.kalenderRepository = kalenderRepository1;
        this.nutzerRepository = nutzerRepository1;
        this.lehrenderRepository = lehrenderRepository1;
        this.studentRepository = studentRepository1;
        this.lehrveranstaltungRepository = lehrveranstaltungRepository;
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
        Student student = studentRepository.findStudentByNutzerId(nutzer);
        if(student != null){
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
        }else if(lehrender != null){
            //Füge Termin für sich selbst
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
            //Füge Termin für alle anderen Teilnehmer der Lehrveranstaltung hinzu
        }
        return "Ok";

    }

}
