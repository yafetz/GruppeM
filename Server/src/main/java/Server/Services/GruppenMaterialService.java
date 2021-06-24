package Server.Services;

import Server.Modell.Gruppenmaterial;
import Server.Modell.Lehrmaterial;
import Server.Repository.GruppenmaterialRepository;
import Server.Repository.ProjektgruppenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class GruppenMaterialService {
    GruppenmaterialRepository gruppenmaterialRepository;
    ProjektgruppenRepository projektgruppenRepository;


    @Autowired
    public GruppenMaterialService(GruppenmaterialRepository gruppenmaterialRepository, ProjektgruppenRepository projektgruppenRepository) {
        this.gruppenmaterialRepository = gruppenmaterialRepository;
        this.projektgruppenRepository = projektgruppenRepository;
    }

    public void addNewMaterial(Long gruppenId, List<MultipartFile> gruppenMaterialList) throws IOException {
        for (MultipartFile gruppenMaterialFile : gruppenMaterialList) {
            Gruppenmaterial gruppenmaterial = new Gruppenmaterial(projektgruppenRepository.findProjektgruppeById(gruppenId),
                    gruppenMaterialFile.getOriginalFilename(),
                    gruppenMaterialFile.getBytes());
            gruppenmaterialRepository.save(gruppenmaterial);
        }
    }
}
