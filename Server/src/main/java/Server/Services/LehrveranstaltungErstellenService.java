package Server.Services;

import Server.Modell.Lehrender;
import Server.Modell.Lehrveranstaltung;
import Server.Modell.Nutzer;
import Server.Modell.TeilnehmerListe;
import Server.Repository.LehrenderRepository;
import Server.Repository.LehrveranstaltungRepository;
import Server.Repository.NutzerRepository;
import Server.Repository.TeilnehmerListeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LehrveranstaltungErstellenService {
    private final LehrenderRepository lehrenderRepository;
    private final LehrveranstaltungRepository lehrveranstaltungRepository;
    private final NutzerRepository nutzerRepository;
    private final TeilnehmerListeRepository teilnehmerListeRepository;

    @Autowired
    public LehrveranstaltungErstellenService(LehrenderRepository lehrenderRepository,TeilnehmerListeRepository teilnehmerListeRepository ,NutzerRepository nutzerRepository,LehrveranstaltungRepository lehrveranstaltungRepository) {
        this.lehrenderRepository = lehrenderRepository;
        this.lehrveranstaltungRepository = lehrveranstaltungRepository;
        this.nutzerRepository = nutzerRepository;
        this.teilnehmerListeRepository=teilnehmerListeRepository;
    }

    public void createNewLehrveranstaltung(String titel, long lehrenderId, String art, String semester){
        Lehrveranstaltung lehrveranstaltung = new Lehrveranstaltung();
        lehrveranstaltung.setTitel(titel);
        lehrveranstaltung.setLehrender(lehrenderRepository.findLehrenderById(lehrenderId));
        lehrveranstaltung.setArt(art);
        lehrveranstaltung.setSemester(semester);
        lehrveranstaltungRepository.save(lehrveranstaltung);
        TeilnehmerListe teilnehmerListe = new TeilnehmerListe();
        Lehrender lehrender = lehrenderRepository.findLehrenderById(lehrenderId);
        teilnehmerListe.setNutzerId(lehrender.getNutzerId());
        teilnehmerListe.setLehrveranstaltung(lehrveranstaltung);
        teilnehmerListeRepository.save(teilnehmerListe);


    }
}
