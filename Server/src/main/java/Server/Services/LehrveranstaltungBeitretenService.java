package Server.Services;


import Server.Modell.Lehrveranstaltung;
import Server.Modell.Student;
import Server.Repository.LehrveranstaltungRepository;
import Server.Repository.StudentRepository;
import Server.Repository.TeilnehmerListeRepository;
import org.springframework.stereotype.Service;

@Service
public class LehrveranstaltungBeitretenService {
    StudentRepository studentRepository;
    LehrveranstaltungRepository lehrveranstaltungRepository;
    TeilnehmerListeRepository teilnehmerListeRepository;

    public LehrveranstaltungBeitretenService(StudentRepository studentRepository, LehrveranstaltungRepository lehrveranstaltungRepository, TeilnehmerListeRepository teilnehmerListeRepository) {
        this.studentRepository = studentRepository;
        this.lehrveranstaltungRepository = lehrveranstaltungRepository;
        this.teilnehmerListeRepository = teilnehmerListeRepository;
    }

    public void beitreten(long lehrveranstaltungsId, long studentId){
        Student student = studentRepository.findStudentById(studentId);
        Lehrveranstaltung lehrveranstaltung = lehrveranstaltungRepository.getOne(lehrveranstaltungsId);

        teilnehmerListeRepository.getAllByLehrveranstaltung(lehrveranstaltungRepository.getOne(lehrveranstaltungsId)).setStudentId(student);



    }

}
