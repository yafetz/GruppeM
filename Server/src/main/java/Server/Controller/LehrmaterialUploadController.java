package Server.Controller;

import Server.Modell.Lehrmaterial;
import Server.Repository.LehrmaterialRepository;
import Server.Services.LehrmaterialStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("lehrveranstaltung/upload")
public class LehrmaterialUploadController {

    private final LehrmaterialStorageService lehrmaterialStorageService;

    @Autowired
    public LehrmaterialUploadController(LehrmaterialStorageService lehrmaterialStorageService) {
        this.lehrmaterialStorageService = lehrmaterialStorageService;
    }

//    @PostMapping(path = "{lehrveranstaltungId}")
//    public void lehrmaterialUpload(@PathVariable("lehrveranstaltungId") Long lehrveranstaltungId,
//                                   @RequestBody Lehrmaterial lehrmaterial) {
//        lehrmaterialStorageService.addNewLehrmaterial(lehrveranstaltungsId, lehrmaterial);
//    }
    @PostMapping
    public void lehrmaterialUpload(@RequestBody Lehrmaterial lehrmaterial,
                                   @RequestParam Long lehrveranstaltungId) {
        lehrmaterialStorageService.addNewLehrmaterial(lehrveranstaltungId, lehrmaterial);
    }
}
