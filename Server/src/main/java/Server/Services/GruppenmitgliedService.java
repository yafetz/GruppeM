package Server.Services;


import Server.Modell.Gruppenmitglied;
import Server.Modell.TeilnehmerListe;
import Server.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GruppenmitgliedService {

    private final ProjektgruppenRepository projektgruppenRepositoryepository;
    private final StudentRepository studentRepository;
    private final GruppenmitgliedRepository gruppenmitgliedRepository;

    @Autowired
    public GruppenmitgliedService(ProjektgruppenRepository projektgruppenRepositoryepository, StudentRepository studentRepository, GruppenmitgliedRepository gruppenmitgliedRepository) {
        this.projektgruppenRepositoryepository = projektgruppenRepositoryepository;
        this.studentRepository = studentRepository;
        this.gruppenmitgliedRepository = gruppenmitgliedRepository;

    }
    public List<Gruppenmitglied> mitglied(long projektgruppeId){

        return gruppenmitgliedRepository.findAllByProjektgruppe(projektgruppenRepositoryepository.findProjektgruppeById(projektgruppeId));

    }
}
