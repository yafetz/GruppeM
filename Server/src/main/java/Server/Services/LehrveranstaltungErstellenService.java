package Server.Services;

import Server.Modell.Lehrender;
import Server.Modell.Lehrveranstaltung;
import Server.Repository.LehrenderRepository;
import Server.Repository.LehrveranstaltungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LehrveranstaltungErstellenService {
    private final LehrenderRepository lehrenderRepository;
    private final LehrveranstaltungRepository lehrveranstaltungRepository;

    @Autowired
    public LehrveranstaltungErstellenService(LehrenderRepository lehrenderRepository, LehrveranstaltungRepository lehrveranstaltungRepository) {
        this.lehrenderRepository = lehrenderRepository;
        this.lehrveranstaltungRepository = lehrveranstaltungRepository;
    }

    public void createNewLehrveranstaltung(String titel, long lehrenderId, String art, String semester){
        Lehrveranstaltung lehrveranstaltung = new Lehrveranstaltung();
        lehrveranstaltung.setTitel(titel);
        lehrveranstaltung.setLehrender(lehrenderRepository.findLehrenderById(lehrenderId));
        lehrveranstaltung.setArt(art);
        lehrveranstaltung.setSemester(semester);
        lehrveranstaltungRepository.save(lehrveranstaltung);
    }
}
