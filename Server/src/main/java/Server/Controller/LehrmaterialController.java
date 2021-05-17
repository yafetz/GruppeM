package Server.Controller;

import Server.Repository.LehrmaterialRepository;
import Server.Repository.LehrveranstaltungRepository;
import Server.Services.LehrmaterialStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("lehrveranstaltung/lehrmaterial")
public class LehrmaterialController {
    @Autowired
    private LehrmaterialStorageService lehrmaterialStorageService;
    @Autowired
    LehrmaterialRepository lehrmaterialRepository;
    @Autowired
    LehrveranstaltungRepository lehrveranstaltungRepository;


    @PostMapping("/upload/{lehrveranstaltungId}")
    public ResponseEntity<String> lehrmaterialUpload(@RequestBody List<MultipartFile> lehrmaterialList,
                                                     @PathVariable Long lehrveranstaltungId) throws IOException {
        for (MultipartFile lehrmaterial : lehrmaterialList) {
            lehrmaterialStorageService.addNewLehrmaterial(lehrveranstaltungId, lehrmaterial);
        }

        return new ResponseEntity<>("erfolgreich hochgeladen", null, HttpStatus.OK);
    }

    @GetMapping("/{lehrveranstaltungsId}")
    public Object alleLehrmaterialien (@PathVariable long lehrveranstaltungsId) {
        return lehrmaterialRepository.findLehrmaterialByLehrveranstaltung(lehrveranstaltungRepository.findLehrveranstaltungById(lehrveranstaltungsId));
    }
}
