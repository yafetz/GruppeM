package Server.Controller;

import Server.Modell.Freundschaft;
import Server.Modell.Nutzer;
import Server.Repository.FreundschaftRepository;
import Server.Repository.NutzerRepository;
import Server.Services.FreundschaftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/freundschaft")
public class FreundschaftController {
    private final FreundschaftService freundschaftService;
    private final NutzerRepository nutzerRepository;
    private final FreundschaftRepository freundschaftRepository;

    @Autowired
    public FreundschaftController(FreundschaftService freundschaftService, NutzerRepository nutzerRepository, FreundschaftRepository freundschaftRepository) {
        this.freundschaftService = freundschaftService;
        this.nutzerRepository = nutzerRepository;
        this.freundschaftRepository = freundschaftRepository;
    }

    @PostMapping("/anfrage")
    public Object anfrage(@RequestParam("anfragender_id") String anfragender_id, @RequestParam("angefragter_id") String angefragter_id){
        long anfrage_id =Long.valueOf(anfragender_id);
        long angefrate_id = Long.valueOf(angefragter_id);
        return freundschaftService.anfrage_senden(anfrage_id, angefrate_id);

    }

    @GetMapping("/profil/status/{meine_id}&{profil_id}")
    public Boolean getStatus(@PathVariable long meine_id, @PathVariable long profil_id){
        if(freundschaftService.getStatus(meine_id,profil_id)!=null){
            return true;
        };
        return false;
    }

    @GetMapping("/meine/{nutzerid}")
    public List<Freundschaft> meineAnfragen(@PathVariable long nutzerid){


        return freundschaftService.meineAnfragen(nutzerid);
    }


    @GetMapping("/{nutzer_id}")
    public List<Freundschaft> meineFreundschaften(@PathVariable long nutzer_id){

        return freundschaftService.meineFreundschaften(nutzer_id);
    }

    @DeleteMapping("meine/delete/{angefragter_id}&{anfragender_id}")
    public ResponseEntity<String> deleteAnfrage(@PathVariable("angefragter_id") long angefragter_id, @PathVariable("anfragender_id") long anfragender_id){



        //freundschaftRepository.delete();
        Freundschaft freundschaft = freundschaftService.getAnfrage(angefragter_id, anfragender_id);
        freundschaftRepository.delete(freundschaft);
        return new ResponseEntity<>("Freundschaft abgelehnt", HttpStatus.OK);
    }
    @GetMapping("meine/akzeptieren/{angefragter_id}&{anfragender_id}")
    public ResponseEntity<String> acceptAnfrage(@PathVariable("angefragter_id") long angefragter_id, @PathVariable("anfragender_id") long anfragender_id){
        Freundschaft freundschaft = freundschaftService.getAnfrage(angefragter_id, anfragender_id);
        freundschaft.setStatus(true);
        freundschaftRepository.save(freundschaft);
        return new ResponseEntity<>("Freundschaft akzeptiert", HttpStatus.OK);
    }








}
