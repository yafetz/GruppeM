package Server.Controller;

import Server.Modell.Lehrender;
import Server.Modell.Nutzer;
import Server.Modell.Student;
import Server.Repository.LehrenderRepository;
import Server.Repository.NutzerRepository;
import Server.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final NutzerRepository nutzerRepository;
    private final StudentRepository studentRepository;
    private final LehrenderRepository lehrenderRepository;

    @Autowired
    public LoginController(NutzerRepository nutzerRepository, StudentRepository studentRepository, LehrenderRepository lehrenderRepository) {
        this.nutzerRepository = nutzerRepository;
        this.studentRepository = studentRepository;
        this.lehrenderRepository = lehrenderRepository;
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
}
