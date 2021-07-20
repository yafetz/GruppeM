package Server.Controller;

import Server.Modell.*;
import Server.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/themen/")
public class ThemenController {
    private final NutzerRepository nutzerRepository;
    private final LehrenderRepository lehrenderRepository;
    private final ThemenRepository themenRepository;
    private final LehrveranstaltungRepository lehrveranstaltungRepository;
    private final LiteraturRepository literaturRepository;
    private final LiteraturThemaRepository literaturThemaRepository;

    @Autowired
    public ThemenController(NutzerRepository nutzerRepository, LehrenderRepository lehrenderRepository, ThemenRepository themenRepository, LehrveranstaltungRepository lehrveranstaltungRepository, LiteraturRepository literaturRepository, LiteraturThemaRepository literaturThemaRepository) {
        this.nutzerRepository = nutzerRepository;
        this.lehrenderRepository = lehrenderRepository;
        this.themenRepository = themenRepository;
        this.lehrveranstaltungRepository = lehrveranstaltungRepository;
        this.literaturRepository = literaturRepository;
        this.literaturThemaRepository = literaturThemaRepository;
    }


    @GetMapping("alle/{lehrender_nutzer_id}/{anfragende_nutzer_id}")
    public List<Thema> alleThemen(@PathVariable long lehrender_nutzer_id,@PathVariable long anfragende_nutzer_id ){
        return themenRepository.findAllByNutzerWhereNotTeilnehmer(nutzerRepository.findNutzerById(lehrender_nutzer_id),nutzerRepository.findNutzerById(anfragende_nutzer_id));
    }

    @GetMapping("alleNochVorhanden/{thema_id}")
    public List<Literatur> alleLiteraturNochVorhanden(@PathVariable long thema_id){
        return literaturThemaRepository.findAllLiteraturWhichAreNotSelected(thema_id);
    }

    @GetMapping("jetzigeLiteratur/{thema_id}")
    public List<Literatur> jetzigeLiteratur(@PathVariable long thema_id){
        return literaturThemaRepository.findAllLiteraturByThema(thema_id);
    }

    @PostMapping("neuesThema")
    public Thema neuesThema(@RequestParam("titel") String titel, @RequestParam("beschreibung") String beschreibung,@RequestParam("lehrveranstaltungId") long lehrveranstaltungId,@RequestParam("nutzerId") long nutzerId){
        Thema thema = new Thema();
        thema.setTitel(titel);
        thema.setBeschreibung(beschreibung);
        thema.setLehrveranstaltung(lehrveranstaltungRepository.findLehrveranstaltungById(lehrveranstaltungId));
        thema.setNutzer(nutzerRepository.findNutzerById(nutzerId));
        themenRepository.save(thema);
        return thema;
    }

    @PostMapping("addLiteraturZuThema")
    public String LiteraturZuThema(@RequestParam("thema_id") Long thema, @RequestParam("literaturliste") List<Long> literaturliste){
        for(int i = 0; i < literaturliste.size(); i++){
            LiteraturThema lt = new LiteraturThema();
            lt.setThema(themenRepository.findThemaById(thema));
            lt.setLiteratur(literaturRepository.findLiteraturById(literaturliste.get(i)));
            literaturThemaRepository.save(lt);
        }
        return "OK";
    }

    @PostMapping("literatur/upload")
    public String neuesThema(@RequestParam("files") List<MultipartFile> multipartFiles) throws IOException {
        for(int i = 0; i < multipartFiles.size(); i++){
            BufferedReader br = new BufferedReader(new InputStreamReader(multipartFiles.get(i).getInputStream()));
            QuizQuestion qq = null;
            List<QuizAnswer> qaList = new ArrayList<>();
            Literatur l = null;
            String file = "";
            while(br.ready()) {
                String line = br.readLine();
                if(line.replaceAll(" ","").startsWith("@")){
                    String type = line.split("\\{")[0].replace("@","");
                    //System.out.println(type);
                    if(l != null){
                    literaturRepository.save(l);
                    }
                    l = new Literatur();
                    l.setType(type);
                    file = "";
                }

                if(l != null){

                    if(line.replaceAll(" ","").toLowerCase(Locale.ROOT).startsWith("author=")){
                        String value = line.split("\\{")[1].replaceAll("\\{","").replaceAll("\\}\\,","");
                        l.setAuthor(value);
                    }else if(line.replaceAll(" ","").toLowerCase(Locale.ROOT).startsWith("year=")){
                        String value = line.split("\\{")[1].replaceAll("\\{","").replaceAll("\\}\\,","");
                        l.setYear(value);
                    }else if(line.replaceAll(" ","").toLowerCase(Locale.ROOT).startsWith("title=")){
                        String value = line.split("\\{")[1].replaceAll("\\{","").replaceAll("\\}\\,","");
                        l.setTitle(value);
                    }else if(line.replaceAll(" ","").toLowerCase(Locale.ROOT).startsWith("publisher=")){
                        String value = line.split("\\{")[1].replaceAll("\\{","").replaceAll("\\}\\,","");
                        l.setPublisher(value);
                    }else if(line.replaceAll(" ","").toLowerCase(Locale.ROOT).startsWith("pages=")){
                        String value = line.split("\\{")[1].replaceAll("\\{","").replaceAll("\\}\\,","");
                        l.setPages(value);
                    }else if(line.replaceAll(" ","").toLowerCase(Locale.ROOT).startsWith("volume=")){
                        String value = line.split("\\{")[1].replaceAll("\\{","").replaceAll("\\}\\,","");
                        l.setVolume(value);
                    }else if(line.replaceAll(" ","").toLowerCase(Locale.ROOT).startsWith("number=")){
                        String value = line.split("\\{")[1].replaceAll("\\{","").replaceAll("\\}\\,","");
                        l.setNumber(value);
                    }else if(line.replaceAll(" ","").toLowerCase(Locale.ROOT).startsWith("booktitle=")){
                        String value = line.split("\\{")[1].replaceAll("\\{","").replaceAll("\\}\\,","");
                        l.setBooktitle(value);
                    }else if(line.replaceAll(" ","").toLowerCase(Locale.ROOT).startsWith("issn=")){
                        String value = line.split("\\{")[1].replaceAll("\\{","").replaceAll("\\}\\,","");
                        l.setIssn(value);
                    }else if(line.replaceAll(" ","").toLowerCase(Locale.ROOT).startsWith("journal=")){
                        String value = line.split("\\{")[1].replaceAll("\\{","").replaceAll("\\}\\,","");
                        l.setJournal(value);
                    }else if(line.replaceAll(" ","").toLowerCase(Locale.ROOT).startsWith("doi=")){
                        String value = line.split("\\{")[1].replaceAll("\\{","").replaceAll("\\}\\,","");
                        l.setDoi(value);
                    }else if(line.replaceAll(" ","").toLowerCase(Locale.ROOT).startsWith("keywords=")){
                        String value = line.split("\\{")[1].replaceAll("\\{","").replaceAll("\\}\\,","");
                        l.setKeywords(value);
                    }else if(line.replaceAll(" ","").toLowerCase(Locale.ROOT).startsWith("address=")){
                        String value = line.split("\\{")[1].replaceAll("\\{","").replaceAll("\\}\\,","");
                        l.setAdress(value);
                    }else if(line.replaceAll(" ","").toLowerCase(Locale.ROOT).startsWith("series=")){
                        String value = line.split("\\{")[1].replaceAll("\\{","").replaceAll("\\}\\,","");
                        l.setSeries(value);
                    }else if(line.replaceAll(" ","").toLowerCase(Locale.ROOT).startsWith("urldate=")){
                        String value = line.split("\\{")[1].replaceAll("\\{","").replaceAll("\\}\\,","");
                        l.setUrldate(value);
                    }else if(line.replaceAll(" ","").toLowerCase(Locale.ROOT).startsWith("file=")){
                        String value = line.split("\\{")[1].replaceAll("\\{","").replaceAll("\\}\\,","");
                        l.setFile(value);
                    }else if(line.replaceAll(" ","").toLowerCase(Locale.ROOT).startsWith("price=")){
                        String value = line.split("\\{")[1].replaceAll("\\{","").replaceAll("\\}\\,","");
                        l.setPrice(value);
                    }else if(line.replaceAll(" ","").toLowerCase(Locale.ROOT).startsWith("isbn=")){
                        String value = line.split("\\{")[1].replaceAll("\\{","").replaceAll("\\}\\,","");
                        l.setIsbn(value);
                    }else if(line.replaceAll(" ","").toLowerCase(Locale.ROOT).startsWith("editor=")){
                        String value = line.split("\\{")[1].replaceAll("\\{","").replaceAll("\\}\\,","");
                        l.setEditor(value);
                    }else if(line.replaceAll(" ","").toLowerCase(Locale.ROOT).startsWith("institution=")){
                        String value = line.split("\\{")[1].replaceAll("\\{","").replaceAll("\\}\\,","");
                        l.setInstitution(value);
                    }else if(line.replaceAll(" ","").toLowerCase(Locale.ROOT).startsWith("url=")){
                        String value = line.split("\\{")[1].replaceAll("\\{","").replaceAll("\\}\\,","");
                        l.setUrl(value);
                    }else if(line.replaceAll(" ","").toLowerCase(Locale.ROOT).startsWith("abstract=")){
                        String value = line.split("\\{")[1].replaceAll("\\{","").replaceAll("\\}\\,","");
                        l.setAbstract(value);
                    }else if(line.replaceAll(" ","").toLowerCase(Locale.ROOT).startsWith("date=")){
                        String value = line.split("\\{")[1].replaceAll("\\{","").replaceAll("\\}\\,","");
                        l.setYear(value);
                    }else if(line.replaceAll(" ","").toLowerCase(Locale.ROOT).startsWith("edition=")){
                        String value = line.split("\\{")[1].replaceAll("\\{","").replaceAll("\\}\\,","");
                        l.setEdition(value);
                    }
                }
            }
        }
        return "OK";
    }
}
