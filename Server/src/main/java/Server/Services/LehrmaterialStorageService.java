package Server.Services;

import Server.Modell.Lehrmaterial;
import Server.Repository.LehrmaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LehrmaterialStorageService {

    private final LehrmaterialRepository lehrmaterialRepository;


    @Autowired
    public LehrmaterialStorageService(LehrmaterialRepository lehrmaterialRepository) {
        this.lehrmaterialRepository = lehrmaterialRepository;
    }

    public void addNewLehrmaterial(Long lehrveranstaltungId, Lehrmaterial lehrmaterial) {

    }
}
