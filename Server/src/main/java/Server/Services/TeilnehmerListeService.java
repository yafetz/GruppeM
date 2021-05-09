package Server.Services;


import Server.Modell.Lehrveranstaltung;
import Server.Modell.Nutzer;
import Server.Modell.Student;
import Server.Modell.TeilnehmerListe;
import Server.Repository.LehrveranstaltungRepository;
import Server.Repository.StudentRepository;
import Server.Repository.TeilnehmerListeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeilnehmerListeService {

    LehrveranstaltungRepository lehrveranstaltungRepository;
    StudentRepository studentRepository;
    TeilnehmerListeRepository teilnehmerListeRepository;

    @Autowired
    public TeilnehmerListeService(LehrveranstaltungRepository lehrveranstaltungRepository, StudentRepository studentRepository,TeilnehmerListeRepository teilnehmerListeRepository) {
        this.lehrveranstaltungRepository = lehrveranstaltungRepository;
        this.studentRepository = studentRepository;
        this.teilnehmerListeRepository=teilnehmerListeRepository;
    }
    public Object teilnehmer(long lehrveranstaltungsId){

     return teilnehmerListeRepository.findAllByLehrveranstaltung(lehrveranstaltungRepository.findAllById(lehrveranstaltungsId));

    }


}
