package Server.Controller;

import Server.Modell.Lehrmaterial;
import Server.Repository.LehrmaterialRepository;
import Server.Services.LehrmaterialStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("lehrveranstaltung/lehrmaterial")
public class LehrmaterialUploadController {

    private final LehrmaterialStorageService lehrmaterialStorageService;

    @Autowired
    public LehrmaterialUploadController(LehrmaterialStorageService lehrmaterialStorageService) {
        this.lehrmaterialStorageService = lehrmaterialStorageService;
    }


    @PostMapping("/upload/{lehrveranstaltungId}")
    public ResponseEntity<String> lehrmaterialUpload(@RequestBody List<MultipartFile> lehrmaterialList,
                                                     @PathVariable Long lehrveranstaltungId) throws IOException {
        for (MultipartFile lehrmaterial : lehrmaterialList) {
            lehrmaterialStorageService.addNewLehrmaterial(lehrveranstaltungId, lehrmaterial);
        }

        return new ResponseEntity<>("erfolgreich hochgeladen", null, HttpStatus.OK);
    }
}
