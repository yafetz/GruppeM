package Server.Controller;

import Server.Modell.Lehrveranstaltung;
import Server.Modell.Nutzer;
import Server.Modell.Student;
import Server.Repository.NutzerRepository;
import Server.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/studenten")
public class StudentController {
    StudentRepository studentRepository;
    NutzerRepository nutzerRepository;

    @Autowired
    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping("/all")
    public List<Student> getAllStudenten() {
        return studentRepository.findAll();
    }












}
