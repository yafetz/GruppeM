package Server.Controller;

import Server.Modell.Lehrender;
import Server.Modell.Nutzer;
import Server.Modell.Student;
import Server.Repository.LehrenderRepository;
import Server.Repository.NutzerRepository;
import Server.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
public class RegistrierController {
    private final StudentRepository studentRepository;
    private final NutzerRepository nutzerRepository;
    private final LehrenderRepository lehrenderRepository;

    @Autowired
    public RegistrierController(StudentRepository studentRepository, NutzerRepository nutzerRepository, LehrenderRepository lehrenderRepository) {
        this.studentRepository = studentRepository;
        this.nutzerRepository = nutzerRepository;
        this.lehrenderRepository = lehrenderRepository;
    }

    @GetMapping("/student/{vorname}&{nachname}&{email}&{passwort}&{studienfach}" +
            "&{hausnummer}&{plz}&{stadt}&{strasse}")
    public String registriere_student(@PathVariable String vorname,
                                      @PathVariable String nachname,
                                      @PathVariable String email,
                                      @PathVariable String passwort,
                                      @PathVariable String studienfach,
                                      @PathVariable int hausnummer,
                                      @PathVariable int plz,
                                      @PathVariable String stadt,
                                      @PathVariable String strasse){
        Nutzer nutzer = new Nutzer();
        nutzer.setEmail(email);
        nutzer.setHausnummer(hausnummer);
        nutzer.setNachname(nachname);
        nutzer.setVorname(vorname);
        nutzer.setPasswort(passwort);
        nutzer.setPlz(plz);
        nutzer.setStadt(stadt);
        nutzer.setStrasse(strasse);
        nutzer.setProfilbild(null);
        nutzerRepository.save(nutzer);
        Student student = new Student();
        student.setMatrikelnummer(generiereMatrikelnummer());
        student.setStudienfach(studienfach);
        student.setNutzerId(nutzer);
        studentRepository.save(student);
        return "OK";
    }

    @GetMapping("/lehrender/{vorname}&{nachname}&{email}&{passwort}&{forschungsgebiet}&{lehrstuhl}" +
            "&{hausnummer}&{plz}&{stadt}&{strasse}")
    public String registriere_lehrender(@PathVariable String vorname,
                                      @PathVariable String nachname,
                                      @PathVariable String email,
                                      @PathVariable String passwort,
                                      @PathVariable String forschungsgebiet,
                                      @PathVariable String lehrstuhl,
                                      @PathVariable int hausnummer,
                                      @PathVariable int plz,
                                      @PathVariable String stadt,
                                      @PathVariable String strasse){
        Nutzer nutzer = new Nutzer();
        nutzer.setEmail(email);
        nutzer.setHausnummer(hausnummer);
        nutzer.setNachname(nachname);
        nutzer.setVorname(vorname);
        nutzer.setPasswort(passwort);
        nutzer.setPlz(plz);
        nutzer.setStadt(stadt);
        nutzer.setStrasse(strasse);
        nutzer.setProfilbild(null);
        nutzerRepository.save(nutzer);
        Lehrender lehrender = new Lehrender();
        lehrender.setLehrstuhl(lehrstuhl);
        lehrender.setForschungsgebiet(forschungsgebiet);
        lehrender.setNutzerId(nutzer);
        lehrenderRepository.save(lehrender);
        return "OK";
    }
    public int generiereMatrikelnummer(){
        int matr = 1000000;
        if(studentRepository.findTopByOrderByIdDesc() != null){
            int id = Math.toIntExact(studentRepository.findTopByOrderByIdDesc().getId());
            return matr+id+1;
        }else{
            return matr;
        }
    }
}
