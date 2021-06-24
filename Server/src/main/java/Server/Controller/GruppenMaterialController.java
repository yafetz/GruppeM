package Server.Controller;

import Server.Modell.Gruppenmaterial;
import Server.Modell.Lehrmaterial;
import Server.Modell.Projektgruppe;
import Server.Repository.GruppenmaterialRepository;
import Server.Repository.NutzerRepository;
import Server.Repository.ProjektgruppenRepository;
import Server.Services.GruppenMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController

@RequestMapping("/gruppenmaterial/")

public class GruppenMaterialController {
    private final NutzerRepository nutzerRepository;
    private final ProjektgruppenRepository projektgruppenRepository;
    private final GruppenmaterialRepository gruppenmaterialRepository;
    private final GruppenMaterialService gruppenMaterialService;


    @Autowired
    public GruppenMaterialController(NutzerRepository nutzerRepository, ProjektgruppenRepository projektgruppenRepository, GruppenmaterialRepository gruppenmaterialRepository, GruppenMaterialService gruppenMaterialService) {
        this.nutzerRepository = nutzerRepository;
        this.projektgruppenRepository = projektgruppenRepository;
        this.gruppenmaterialRepository = gruppenmaterialRepository;
        this.gruppenMaterialService = gruppenMaterialService;
    }


    @PostMapping("/upload")
    public ResponseEntity<String> lehrmaterialUpload(@RequestParam("files") List<MultipartFile> multipartFiles,
                                                     @RequestParam("gruppenId") Long gruppenId) throws IOException {

        gruppenMaterialService.addNewMaterial(gruppenId, multipartFiles);
        return new ResponseEntity<>("Servernachricht: Material erfolgreich hochgeladen!", null, HttpStatus.OK);
    }
    @GetMapping("/{gruppenId}")
    public List<Gruppenmaterial> alleMaterialien (@PathVariable long gruppenId) {
        long id = gruppenId;
        Projektgruppe event = projektgruppenRepository.findProjektgruppeById(id);
        List<Gruppenmaterial> materials = gruppenmaterialRepository.findGruppenmaterialByProjektgruppeId(gruppenId);
        return materials;
    }

    @GetMapping("/datei/{material_id}")
    public Gruppenmaterial getGruppenmaterial(@PathVariable long material_id){
        long id = material_id;
        return gruppenmaterialRepository.findGruppenmaterialById(id);
    }
    @GetMapping("/download/{id}")
    public byte[] MaterialDownload(@PathVariable long id){
        Gruppenmaterial material =  gruppenmaterialRepository.findGruppenmaterialById(id);
        return material.getDatei();
    }

}
