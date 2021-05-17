package Server.Services;

import Server.Modell.Lehrmaterial;
import Server.Repository.LehrmaterialRepository;
import Server.Repository.LehrveranstaltungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Stream;

@Service
public class LehrmaterialStorageService {
    @Autowired
    private LehrmaterialRepository lehrmaterialRepository;
    @Autowired
    private LehrveranstaltungRepository lehrveranstaltungRepository;


    public void addNewLehrmaterial(Long lehrveranstaltungId, MultipartFile lehrmaterialFile) throws IOException {
        Lehrmaterial lehrmaterial = new Lehrmaterial(lehrveranstaltungRepository.findLehrveranstaltungById(lehrveranstaltungId),
                lehrmaterialFile.getName(),
                lehrmaterialFile.getContentType(),
                lehrmaterialFile.getBytes());
        lehrmaterialRepository.save(lehrmaterial);
    }

    public Lehrmaterial getLehrmaterial() {

        return null;
    }

    public Stream<Lehrmaterial> getAllLehrmaterial() {

        return null;
    }
}
