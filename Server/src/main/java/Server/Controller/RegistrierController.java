package Server.Controller;

import Server.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegistrierController {

    private final StudentRepository student;
    @Autowired
    public RegistrierController(StudentRepository student){

        this.student = student;
    }

    @GetMapping("/student/{vorname}&{nachnamen}&{email}&{passwort}&{studienfach}" +
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
        return "OK";
    }

    public int generiereMatrikelnummer(){
        int matr = 1000000;
        return matr+student.findTopByOrderByIdDesc()+1;
    }
}
