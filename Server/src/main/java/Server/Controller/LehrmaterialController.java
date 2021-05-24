package Server.Controller;
import Server.Modell.Lehrender;
import Server.Modell.Lehrmaterial;
import Server.Modell.Lehrveranstaltung;
import Server.Modell.TeilnehmerListe;
import Server.Repository.*;
import Server.Services.LehrmaterialStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

@RestController
@RequestMapping("/lehrmaterial/")


public class LehrmaterialController {
    private final LehrmaterialStorageService lehrmaterialStorageService;
    private final LehrmaterialRepository lehrmaterialRepository;
    private final LehrveranstaltungRepository lehrveranstaltungRepository;
    private final LehrenderRepository lehrenderRepository;
    private final NutzerRepository nutzerRepository;
    private final TeilnehmerListeRepository teilnehmerListeRepository;

    @Autowired
    public LehrmaterialController(LehrmaterialStorageService lehrmaterialStorageService, LehrmaterialRepository lehrmaterialRepository, LehrveranstaltungRepository lehrveranstaltungRepository, LehrenderRepository lehrenderRepository, NutzerRepository nutzerRepository, TeilnehmerListeRepository teilnehmerListeRepository) {
        this.lehrmaterialStorageService = lehrmaterialStorageService;
        this.lehrmaterialRepository = lehrmaterialRepository;
        this.lehrveranstaltungRepository = lehrveranstaltungRepository;
        this.lehrenderRepository = lehrenderRepository;
        this.nutzerRepository = nutzerRepository;
        this.teilnehmerListeRepository = teilnehmerListeRepository;
    }


    @GetMapping("/{lehrveranstaltungsId}")
    public Object alleLehrmaterialien (@PathVariable long lehrveranstaltungsId) {
       long id = lehrveranstaltungsId;
       Lehrveranstaltung event = lehrveranstaltungRepository.findLehrveranstaltungById(id);
       List<Lehrmaterial> materials = lehrmaterialRepository.findLehrmaterialByLehrveranstaltung(event);
       return materials;
   }


    @PostMapping("/upload")
    public ResponseEntity<String> lehrmaterialUpload(@RequestParam("files") List<MultipartFile> multipartFiles,
                                                     @RequestParam("lehrveranstaltungId") Long lehrveranstaltungId) throws IOException {

        lehrmaterialStorageService.addNewLehrmaterial(lehrveranstaltungId, multipartFiles);
        return new ResponseEntity<>("Servernachricht: Lehrmaterial erfolgreich hochgeladen!", null, HttpStatus.OK);
    }

    @GetMapping("/download/{id}")
    public byte[] lehrmaterialDownload(@PathVariable long id){
        Lehrmaterial lehr =  lehrmaterialRepository.findLehrmaterialById(id);
        return lehr.getDatei();
    }

    @PostMapping("/csv")
    public ResponseEntity<String> CsvUpload(@RequestParam("files") List<MultipartFile> multipartFiles,
                                            @RequestParam("nutzerId") Long nutzerId) throws IOException {
        for (int i = 0; i < multipartFiles.size(); i++ ) {
            System.out.println(multipartFiles.get(i).getOriginalFilename());
            System.out.println("------------------------------------------");
            BufferedReader br = new BufferedReader(new InputStreamReader(multipartFiles.get(i).getInputStream()));
            int countLine = 0;
            while(br.ready()) {
                String line = br.readLine();
                if(countLine == 0){
                    String[] header = line.split(";");
                    if(header[0].equalsIgnoreCase("Titel") && header[1].equalsIgnoreCase("Veranstaltungsart") && header[2].equalsIgnoreCase("Semester") && header.length == 3) {

                    } else {
                        //CSV ungültig
                    }
                } else {
                    String[] components = line.split(";");
                    if(components.length == 3 && !components[0].equalsIgnoreCase("") && !components[1].equalsIgnoreCase("") && !components[2].equalsIgnoreCase("") ){

                            String[] altsemester = components[2].split(" ");
                            String Veranstaltungsart = Art(components[1].replace(" ",""));
                            if(!Veranstaltungsart.equalsIgnoreCase("")){
                                String semester = Semester(altsemester);
                                if(!semester.equalsIgnoreCase("")){
                                    String titel = components[0];
                                    if(lehrveranstaltungRepository.existsIfTitelAndArtAndSemester(titel,Veranstaltungsart,semester) == false){
                                        Lehrveranstaltung add = new Lehrveranstaltung();
                                        add.setTitel(titel);
                                        add.setArt(Veranstaltungsart);
                                        add.setSemester(semester);
                                        Lehrender l = lehrenderRepository.findLehrenderByNutzerId(nutzerRepository.findNutzerById(nutzerId));
                                        add.setLehrender(l);
                                        lehrveranstaltungRepository.save(add);
                                        TeilnehmerListe teilnehmerListe = new TeilnehmerListe();
                                        teilnehmerListe.setNutzerId(l.getNutzerId());
                                        teilnehmerListe.setLehrveranstaltung(add);
                                        teilnehmerListeRepository.save(teilnehmerListe);
                                    }
                                }
                            }
                    }
                }
                countLine++;
            }
        }
        return new ResponseEntity<>("Servernachricht: CSV-Datei erfolgreich hochgeladen!", null, HttpStatus.OK);
    }

    public String Art(String art){
        if(art.equalsIgnoreCase("vorlesung")){
            return "Vorlesung";
        }else if(art.equalsIgnoreCase("seminar")){
            return "Seminar";
        }
        return "";
    }

    public String Semester(String[] semester){
        String semesterErgebnis = "";
        if(semester.length == 2 && (semester[0].equalsIgnoreCase("SoSe") || semester[0].equalsIgnoreCase("WiSe")) ){
            String[] semesterJahr1 = semester[1].split("/");
            String[] semesterJahr2 = semester[1].split("-");
            if(semesterJahr1.length == 2 && semesterJahr2.length == 1){
                if(semesterJahr1[0].matches("[0-9]+") && semesterJahr1[1].matches("[0-9]+")){
                    char[] jahr1 = semesterJahr1[0].toCharArray();
                    String SemesterJahrErgebnis1 = "";
                    if(jahr1.length == 2){
                        SemesterJahrErgebnis1 = "20"+semesterJahr1[0];
                    }else{
                        SemesterJahrErgebnis1 = semesterJahr1[0];
                    }
                    char[] jahr2 = semesterJahr1[1].toCharArray();
                    String SemesterJahrErgebnis2 = "";
                    if(jahr2.length == 2){
                        SemesterJahrErgebnis2 = "20"+semesterJahr1[1];
                    }else{
                        SemesterJahrErgebnis2 = semesterJahr1[1];
                    }
                    if((Integer.valueOf(SemesterJahrErgebnis1)+1) == Integer.valueOf(SemesterJahrErgebnis2) ){
                        //System.out.println("Abfrage original: "+ components[2] + " gesplittet: "+ semester[0]);
                        if(semester[0].equalsIgnoreCase("SoSe")){
                            semesterErgebnis = "SoSe "+SemesterJahrErgebnis1;
                        }else if(semester[0].equalsIgnoreCase("WiSe")){
                            semesterErgebnis = "WiSe "+SemesterJahrErgebnis1+"/"+SemesterJahrErgebnis2;
                        }
                    }else{
                        if(semester[0].equalsIgnoreCase("SoSe")){
                            semesterErgebnis = "SoSe "+SemesterJahrErgebnis1;
                        }else if(semester[0].equalsIgnoreCase("WiSe")){
                            semesterErgebnis = "WiSe "+SemesterJahrErgebnis1+"/"+ (Integer.valueOf(SemesterJahrErgebnis1)+1);
                        }
                    }
                }
            }else if(semesterJahr2.length == 2 && semesterJahr1.length == 1){
                if(semesterJahr2[0].matches("[0-9]+") && semesterJahr2[1].matches("[0-9]+")){
                    char[] jahr1 = semesterJahr2[0].toCharArray();
                    String SemesterJahrErgebnis1 = "";
                    if(jahr1.length == 2){
                        SemesterJahrErgebnis1 = "20"+semesterJahr2[0];
                    }else{
                        SemesterJahrErgebnis1 = semesterJahr2[0];
                    }
                    char[] jahr2 = semesterJahr2[1].toCharArray();
                    String SemesterJahrErgebnis2 = "";
                    if(jahr2.length == 2){
                        SemesterJahrErgebnis2 = "20"+semesterJahr2[1];
                    }else{
                        SemesterJahrErgebnis2 = semesterJahr2[1];
                    }
                    if((Integer.valueOf(SemesterJahrErgebnis1)+1) == Integer.valueOf(SemesterJahrErgebnis2) ){
                        if(semester[0].equalsIgnoreCase("SoSe")){
                            semesterErgebnis = "SoSe "+SemesterJahrErgebnis1;
                        }else if(semester[0].equalsIgnoreCase("WiSe")){
                            semesterErgebnis = "WiSe "+SemesterJahrErgebnis1+"/"+SemesterJahrErgebnis2;
                        }
                    }else{
                        if(semester[0].equalsIgnoreCase("SoSe")){
                            semesterErgebnis = "SoSe "+SemesterJahrErgebnis1;
                        }else if(semester[0].equalsIgnoreCase("WiSe")){
                            semesterErgebnis = "WiSe "+SemesterJahrErgebnis1+"/"+ (Integer.valueOf(SemesterJahrErgebnis1)+1);
                        }
                    }
                }


            }else if(semesterJahr2.length == 1 && semesterJahr1.length == 1){
                if(semester[1].matches("[0-9]+")){
                    char[] jahr = semester[1].toCharArray();
                    String SemesterJahrErgebnis = "";
                    if(jahr.length == 2){
                        SemesterJahrErgebnis = "20"+semester[1];
                    }else{
                        SemesterJahrErgebnis = semester[1];
                    }
                    if(semester[0].equalsIgnoreCase("SoSe")){
                        semesterErgebnis = "SoSe "+SemesterJahrErgebnis;
                    }else if(semester[0].equalsIgnoreCase("WiSe")){
                        semesterErgebnis = "WiSe "+SemesterJahrErgebnis+"/"+(Integer.valueOf(SemesterJahrErgebnis)+1);
                    }
                }
            }
        }
        return semesterErgebnis;
    }

}
