package Server.Services;


import Server.Modell.Lehrveranstaltung;
import Server.Modell.Student;
import Server.Modell.TeilnehmerListe;
import Server.Repository.LehrveranstaltungRepository;
import Server.Repository.StudentRepository;
import Server.Repository.TeilnehmerListeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LehrveranstaltungBeitretenService {
    StudentRepository studentRepository;
    LehrveranstaltungRepository lehrveranstaltungRepository;
    TeilnehmerListeRepository teilnehmerListeRepository;

    @Autowired
    public LehrveranstaltungBeitretenService(StudentRepository studentRepository, LehrveranstaltungRepository lehrveranstaltungRepository, TeilnehmerListeRepository teilnehmerListeRepository) {
        this.studentRepository = studentRepository;
        this.lehrveranstaltungRepository = lehrveranstaltungRepository;
        this.teilnehmerListeRepository = teilnehmerListeRepository;
    }

    public Object beitreten(long lehrveranstaltungsId, long studentId){
        Student student = studentRepository.findStudentById(studentId);
        Lehrveranstaltung lehrveranstaltung = lehrveranstaltungRepository.findLehrveranstaltungById(lehrveranstaltungsId);
        TeilnehmerListe teilnehmerListe = new TeilnehmerListe();

        //TeilnehmerListe teilnehmerListe = teilnehmerListeRepository.findByLehrveranstaltung(lehrveranstaltungsId);
        teilnehmerListe.setLehrveranstaltung(lehrveranstaltung);
        teilnehmerListe.setStudentId(student);
        teilnehmerListeRepository.save(teilnehmerListe);

        return teilnehmerListe;

    }

}
