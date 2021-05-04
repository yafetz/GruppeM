package Server.Controller;

import Server.Modell.Student;
import Server.Services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class Login {

    @GetMapping("/{matrikelnummer}&{passwort}")
    public Student getStudent(@PathVariable int matrikelnummer, @PathVariable String passwort){
        return null;
    }
}
