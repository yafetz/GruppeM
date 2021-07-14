package Server.Services;

import Server.Modell.Freundschaft;
import Server.Modell.Nutzer;
import Server.Repository.FreundschaftRepository;
import Server.Repository.NutzerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FreundschaftService {

    private final FreundschaftRepository freundschaftRepository;
    private final NutzerRepository nutzerRepository;


    @Autowired
    public FreundschaftService(FreundschaftRepository freundschaftRepository, NutzerRepository nutzerRepository) {
        this.freundschaftRepository = freundschaftRepository;
        this.nutzerRepository = nutzerRepository;
    }

    public List<Freundschaft> meineAnfragen(long id){

       return freundschaftRepository.getAllFreundschaftsAnfragen(nutzerRepository.findNutzerById(id));

    }

    public List<Freundschaft>meineFreundschaften(long id){
        return freundschaftRepository.getFreundschaften(nutzerRepository.findNutzerById(id));
    }

    public Freundschaft getAnfrage(long id1, long id2){
        Nutzer angefragter_id = nutzerRepository.findNutzerById(id1);
        Nutzer anfragender_id = nutzerRepository.findNutzerById(id2);
        freundschaftRepository.getFreundschaft(angefragter_id, anfragender_id);
        Freundschaft freundschaft = freundschaftRepository.getFreundschaft(anfragender_id,angefragter_id);

        return freundschaftRepository.getFreundschaft(anfragender_id, angefragter_id);
    }


    public Freundschaft getStatus(long meine_id, long profil_id){
        return freundschaftRepository.getStatus(nutzerRepository.findNutzerById(meine_id), nutzerRepository.findNutzerById(profil_id));

    }

    public Object anfrage_senden(long anfragender_id, long angefragter_id){
        Freundschaft freundschaft = new Freundschaft();
        freundschaft.setAnfragender_nutzer(nutzerRepository.findNutzerById(anfragender_id));
        freundschaft.setAngefragter_nutzer(nutzerRepository.findNutzerById(angefragter_id));
        freundschaftRepository.save(freundschaft);

        return freundschaft;

    }

}
