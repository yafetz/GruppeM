package Server.Services;


import Server.Modell.Gruppenmitglied;
import Server.Modell.TeilnehmerListe;
import Server.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import Server.Modell.Gruppenmitglied;
import Server.Modell.Projektgruppe;
import Server.Modell.Student;
import Server.Repository.GruppenmitgliedRepository;
import Server.Repository.ProjektgruppenRepository;
import Server.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GruppenmitgliedService {
    private final GruppenmitgliedRepository gruppenmitgliedRepository;
    private final ProjektgruppenRepository projektgruppenRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public GruppenmitgliedService(GruppenmitgliedRepository gruppenmitgliedRepository, ProjektgruppenRepository projektgruppenRepository, StudentRepository studentRepository) {
        this.gruppenmitgliedRepository = gruppenmitgliedRepository;
        this.projektgruppenRepository = projektgruppenRepository;
        this.studentRepository = studentRepository;
    }


    public void addMitglieder(Projektgruppe projektgruppe, List<Long> studentId) {
        for (Long id : studentId) {
            Student student = studentRepository.findStudentById(id);
            Gruppenmitglied neu = new Gruppenmitglied(student, projektgruppe);
            gruppenmitgliedRepository.save(neu);
        }
    }
    public List<Gruppenmitglied> mitglied(long projektgruppeId){

        return gruppenmitgliedRepository.findAllByProjektgruppe(projektgruppenRepository.findProjektgruppeById(projektgruppeId));

    }
}
