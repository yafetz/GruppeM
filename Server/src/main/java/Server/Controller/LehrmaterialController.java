package Server.Controller;
import Server.Modell.Lehrender;
import Server.Modell.Lehrmaterial;
import Server.Modell.Lehrveranstaltung;
import Server.Modell.TeilnehmerListe;
import Server.Repository.*;
import Server.Services.LehrmaterialStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

@RestController
@RequestMapping("/lehrmaterial/")


public class LehrmaterialController {
    private final LehrmaterialStorageService lehrmaterialStorageService;
    private final LehrmaterialRepository lehrmaterialRepository;
    private final LehrveranstaltungRepository lehrveranstaltungRepository;
    private final LehrenderRepository lehrenderRepository;
    private final NutzerRepository nutzerRepository;
    private final TeilnehmerListeRepository teilnehmerListeRepository;

    @Autowired
    public LehrmaterialController(LehrmaterialStorageService lehrmaterialStorageService, LehrmaterialRepository lehrmaterialRepository, LehrveranstaltungRepository lehrveranstaltungRepository, LehrenderRepository lehrenderRepository, NutzerRepository nutzerRepository, TeilnehmerListeRepository teilnehmerListeRepository) {
        this.lehrmaterialStorageService = lehrmaterialStorageService;
        this.lehrmaterialRepository = lehrmaterialRepository;
        this.lehrveranstaltungRepository = lehrveranstaltungRepository;
        this.lehrenderRepository = lehrenderRepository;
        this.nutzerRepository = nutzerRepository;
        this.teilnehmerListeRepository = teilnehmerListeRepository;
    }


    @GetMapping("/{lehrveranstaltungsId}")
    public Object alleLehrmaterialien (@PathVariable long lehrveranstaltungsId) {
       long id = lehrveranstaltungsId;
       Lehrveranstaltung event = lehrveranstaltungRepository.findLehrveranstaltungById(id);
       List<Lehrmaterial> materials = lehrmaterialRepository.findLehrmaterialByLehrveranstaltung(event);
       return materials;
   }


    @PostMapping("/upload")
    public ResponseEntity<String> lehrmaterialUpload(@RequestParam("files") List<MultipartFile> multipartFiles,
                                                     @RequestParam("lehrveranstaltungId") Long lehrveranstaltungId) throws IOException {

        lehrmaterialStorageService.addNewLehrmaterial(lehrveranstaltungId, multipartFiles);
        return new ResponseEntity<>("Servernachricht: Lehrmaterial erfolgreich hochgeladen!", null, HttpStatus.OK);
    }

    @GetMapping("/download/{id}")
    public byte[] lehrmaterialDownload(@PathVariable long id){
        Lehrmaterial lehr =  lehrmaterialRepository.findLehrmaterialById(id);
        return lehr.getDatei();
    }

}
