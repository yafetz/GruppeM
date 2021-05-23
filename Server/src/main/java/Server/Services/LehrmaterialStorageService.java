package Server.Services;

import Server.Modell.Lehrmaterial;
import Server.Repository.LehrmaterialRepository;
import Server.Repository.LehrveranstaltungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@Service
public class LehrmaterialStorageService {
    private final LehrmaterialRepository lehrmaterialRepository;
    private final LehrveranstaltungRepository lehrveranstaltungRepository;

    @Autowired
    public LehrmaterialStorageService(LehrmaterialRepository lehrmaterialRepository, LehrveranstaltungRepository lehrveranstaltungRepository) {
        this.lehrmaterialRepository = lehrmaterialRepository;
        this.lehrveranstaltungRepository = lehrveranstaltungRepository;
    }


    public void addNewLehrmaterial(Long lehrveranstaltungId, List<MultipartFile> lehrmaterialList) throws IOException {
        for (MultipartFile lehrmaterialFile : lehrmaterialList) {
            Lehrmaterial lehrmaterial = new Lehrmaterial(lehrveranstaltungRepository.findLehrveranstaltungById(lehrveranstaltungId),
                    lehrmaterialFile.getOriginalFilename(),
                    lehrmaterialFile.getBytes());
            lehrmaterialRepository.save(lehrmaterial);
        }
    }

    public void readCsv(MultipartFile csv){

    }

    public Lehrmaterial getLehrmaterial() {

        return null;
    }

    public Stream<Lehrmaterial> getAllLehrmaterial() {

        return null;
    }
}
