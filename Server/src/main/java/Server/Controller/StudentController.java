package Server.Controller;

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
    private final StudentRepository studentRepository;
    private final NutzerRepository nutzerRepository;

    @Autowired
    public StudentController(StudentRepository studentRepository, NutzerRepository nutzerRepository) {
        this.studentRepository = studentRepository;
        this.nutzerRepository = nutzerRepository;
    }

    @GetMapping("/all")
    public List<Student> getAllStudenten() {
        return studentRepository.findAll();
    }












}
