package Server.Controller;

import Server.Modell.Nutzer;
import Server.Modell.Student;
import Server.Repository.NutzerRepository;
import Server.Repository.StudentRepository;
import Server.Services.StudentService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/login")
public class Login {
    @Autowired
    NutzerRepository nutzerRepository;
    @Autowired
    StudentRepository studentRepository;
    @GetMapping("/{matrikelnummer}&{passwort}")
    public Nutzer getStudent(@PathVariable int matrikelnummer, @PathVariable String email, @PathVariable String passwort){
        Student student = studentRepository.findStudentByMatrikelnummer(matrikelnummer);
        if(student == null){
            Nutzer nutzer = nutzerRepository.findNutzerByEmail(email);
        }else{

        }
        return student.getNutzer_id();
    }
}
