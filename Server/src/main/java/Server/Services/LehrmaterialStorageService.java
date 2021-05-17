package Server.Services;

import Server.Modell.Lehrmaterial;
import Server.Repository.LehrmaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Stream;

@Service
public class LehrmaterialStorageService {
    @Autowired
    private LehrmaterialRepository lehrmaterialRepository;


    public void addNewLehrmaterial(Long lehrveranstaltungId, MultipartFile lehrmaterial) {
//        lehrmaterialRepository.save();
    }

    public Lehrmaterial getLehrmaterial() {

        return null;
    }

    public Stream<Lehrmaterial> getAllLehrmaterial() {

        return null;
    }
}
