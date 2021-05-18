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


    @PostMapping("/upload")
    public ResponseEntity<String> lehrmaterialUpload(@RequestParam("files") List<MultipartFile> multipartFiles,
                                                     @RequestParam("lehrveranstaltungId") Long lehrveranstaltungId) throws IOException {

        lehrmaterialStorageService.addNewLehrmaterial(lehrveranstaltungId, multipartFiles);
        return new ResponseEntity<>("Servernachricht: Erfolgreich hochgeladen!", null, HttpStatus.OK);
    }

    @GetMapping("/{lehrveranstaltungsId}")
    public Object getAlleLehrmaterialien (@PathVariable long lehrveranstaltungsId) {
        return lehrmaterialRepository.findLehrmaterialByLehrveranstaltung(lehrveranstaltungRepository.findLehrveranstaltungById(lehrveranstaltungsId));
    }
}
