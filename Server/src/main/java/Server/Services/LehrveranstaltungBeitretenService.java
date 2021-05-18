package Server.Services;


import Server.Modell.Lehrveranstaltung;
import Server.Modell.Nutzer;
import Server.Modell.Student;
import Server.Modell.TeilnehmerListe;
import Server.Repository.LehrveranstaltungRepository;
import Server.Repository.NutzerRepository;
import Server.Repository.StudentRepository;
import Server.Repository.TeilnehmerListeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LehrveranstaltungBeitretenService {
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    LehrveranstaltungRepository lehrveranstaltungRepository;
    @Autowired
    TeilnehmerListeRepository teilnehmerListeRepository;
    @Autowired
    NutzerRepository nutzerRepository;

   /* @Autowired
    public LehrveranstaltungBeitretenService(StudentRepository studentRepository,TeilnehmerListeRepository teilnehmerListeRepository, LehrveranstaltungRepository lehrveranstaltungRepository, NutzerRepository nutzerRepository) {
        this.studentRepository = studentRepository;
        this.lehrveranstaltungRepository = lehrveranstaltungRepository;
        this.teilnehmerListeRepository = teilnehmerListeRepository;
        this.nutzerRepository = nutzerRepository;
    }
*/
    public Object beitreten(long lehrveranstaltungsId, long nutzerId){
        //Student student = studentRepository.findStudentById(nutzerId);
        Nutzer nutzer = nutzerRepository.findNutzerById(nutzerId);
        Lehrveranstaltung lehrveranstaltung = lehrveranstaltungRepository.findLehrveranstaltungById(lehrveranstaltungsId);
        TeilnehmerListe teilnehmerListe = new TeilnehmerListe();

        //TeilnehmerListe teilnehmerListe = teilnehmerListeRepository.findByLehrveranstaltung(lehrveranstaltungsId);
        teilnehmerListe.setLehrveranstaltung(lehrveranstaltung);
        teilnehmerListe.setNutzerId(nutzer);
        teilnehmerListeRepository.save(teilnehmerListe);

        return teilnehmerListe;

    }

}