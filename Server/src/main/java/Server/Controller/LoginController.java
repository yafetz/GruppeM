package Server.Controller;

import Server.Modell.DatumUndUhrzeit;
import Server.Modell.Lehrender;
import Server.Modell.Nutzer;
import Server.Modell.Student;
import Server.Repository.DatumUhrzeitRepository;
import Server.Repository.LehrenderRepository;
import Server.Repository.NutzerRepository;
import Server.Repository.StudentRepository;
import Server.Scheduler.KalenderScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final NutzerRepository nutzerRepository;
    private final StudentRepository studentRepository;
    private final LehrenderRepository lehrenderRepository;
    private final DatumUhrzeitRepository datumUhrzeitRepository;
    private Clock clock;
    private KalenderScheduler kalender;

    @Autowired
    public LoginController(NutzerRepository nutzerRepository, StudentRepository studentRepository, LehrenderRepository lehrenderRepository, DatumUhrzeitRepository datumUhrzeitRepository) {
        this.nutzerRepository = nutzerRepository;
        this.studentRepository = studentRepository;
        this.lehrenderRepository = lehrenderRepository;
        this.datumUhrzeitRepository = datumUhrzeitRepository;
    }

    @GetMapping("/{user}&{passwort}")
    public Object getLogin(@PathVariable String user, @PathVariable String passwort){
        Lehrender lehrender = new Lehrender();
        Student student = new Student();
        if(user.contains("@")){
            Nutzer nutzer = nutzerRepository.findNutzerByEmail(user);
            //check if nutzer is found
            if(nutzer != null){
                //check if Lehrender or Student
                student = studentRepository.findStudentByNutzerId(nutzer);
                lehrender = lehrenderRepository.findLehrenderByNutzerId(nutzer);
                if(student == null && lehrender != null){
                    //check if password is correct
                    if(checkPassword(lehrender,passwort)){
                        return lehrender;
                    }
                }else if(lehrender == null && student != null){
                    if(checkPassword(student,passwort)){
                        return student;
                    }
                }
            }
        }else if(user.matches("[0-9]+") && user.length() == 7){
            student = studentRepository.findStudentByMatrikelnummer(Integer.valueOf(user));
            if(student != null){
                if(checkPassword(student,passwort)){
                    return student;
                }
            }
        }
        return null;
    }

    public boolean checkPassword(Object object, String passwort){
        if(object instanceof Student){
           if( ((Student) object).getNutzerId().getPasswort().equals(passwort) ){
               return true;
           }else{
               return false;
           }
        }else if( object instanceof Lehrender){
            if( ((Lehrender) object).getNutzerId().getPasswort().equals(passwort) ){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }


    @PostMapping("/uploadDateAndTime")
    public String uploadDate(@RequestParam("datum") String datum) {
        DatumUndUhrzeit date= new DatumUndUhrzeit();
        date.setDatum(LocalDateTime.parse(datum));

        datumUhrzeitRepository.deleteAll();
        datumUhrzeitRepository.save(date);


        System.out.println(datum);





        return "ok";
    }

    @GetMapping("/getDate")
    public LocalDateTime getDate() {
        List<DatumUndUhrzeit> listdatum = datumUhrzeitRepository.findAll();
        LocalDateTime datum = listdatum.get(0).getDatum();

        return datum;
    }






}
