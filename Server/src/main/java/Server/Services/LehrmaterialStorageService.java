package Server.Services;

import Server.Modell.Lehrmaterial;
import Server.Repository.LehrmaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LehrmaterialStorageService {

    private final LehrmaterialRepository lehrmaterialRepository;

    @Value("${upload.path}")
    private String path;



    @Autowired
    public LehrmaterialStorageService(LehrmaterialRepository lehrmaterialRepository) {
        this.lehrmaterialRepository = lehrmaterialRepository;
    }

    public void addNewLehrmaterial(Long lehrveranstaltungId, Lehrmaterial lehrmaterial) {

    }
}
