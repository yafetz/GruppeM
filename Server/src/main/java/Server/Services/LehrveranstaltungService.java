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
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class LehrveranstaltungService {
    private final LehrenderRepository lehrenderRepository;
    private final LehrveranstaltungRepository lehrveranstaltungRepository;
    private final NutzerRepository nutzerRepository;
    private final TeilnehmerListeRepository teilnehmerListeRepository;

    @Autowired
    public LehrveranstaltungService( LehrenderRepository lehrenderRepository, TeilnehmerListeRepository teilnehmerListeRepository, NutzerRepository nutzerRepository, LehrveranstaltungRepository lehrveranstaltungRepository) {
        this.lehrenderRepository = lehrenderRepository;
        this.lehrveranstaltungRepository = lehrveranstaltungRepository;
        this.nutzerRepository = nutzerRepository;
        this.teilnehmerListeRepository=teilnehmerListeRepository;
    }
    public Object beitreten(long lehrveranstaltungsId, long nutzerId){

        Nutzer nutzer = nutzerRepository.findNutzerById(nutzerId);
        Lehrveranstaltung lehrveranstaltung = lehrveranstaltungRepository.findLehrveranstaltungById(lehrveranstaltungsId);
        TeilnehmerListe teilnehmerListe = new TeilnehmerListe();
        teilnehmerListe.setLehrveranstaltung(lehrveranstaltung);
        teilnehmerListe.setNutzerId(nutzer);
        teilnehmerListeRepository.save(teilnehmerListe);

        return teilnehmerListe;

    }
    public void createNewLehrveranstaltung(String titel, long lehrenderId, String art, String semester){
        Lehrveranstaltung lehrveranstaltung = new Lehrveranstaltung();
        lehrveranstaltung.setTitel(titel);
        lehrveranstaltung.setLehrender(lehrenderRepository.findLehrenderById(lehrenderId));
        lehrveranstaltung.setArt(art);
        lehrveranstaltung.setSemester(semester);
        lehrveranstaltungRepository.save(lehrveranstaltung);
        TeilnehmerListe teilnehmerListe = new TeilnehmerListe();
        //Lehrender lehrender = lehrenderRepository.findLehrenderById(lehrenderId);
        Lehrender lehrender = lehrenderRepository.findLehrenderById(lehrenderId);
        teilnehmerListe.setNutzerId(lehrender.getNutzerId());
        teilnehmerListe.setLehrveranstaltung(lehrveranstaltung);
        teilnehmerListeRepository.save(teilnehmerListe);

    }
    public void updateLehrveranstaltung(String titel, long lehrenderId, String art, String semester, long id, Nutzer nutzer){
        Lehrveranstaltung lehrveranstaltung = lehrveranstaltungRepository.findLehrveranstaltungById(id);
        System.out.println(lehrveranstaltung);
        lehrveranstaltung.setTitel(titel);
        lehrveranstaltung.setLehrender(lehrenderRepository.findLehrenderById(lehrenderId));
        lehrveranstaltung.setArt(art);
        lehrveranstaltung.setSemester(semester);
        lehrveranstaltungRepository.save(lehrveranstaltung);
        TeilnehmerListe teilnehmerListe = teilnehmerListeRepository.findTeilnehmerListeByLehrveranstaltungAndNutzerId(lehrveranstaltung,nutzer);
        teilnehmerListe.setNutzerId(nutzer);
        teilnehmerListe.setLehrveranstaltung(lehrveranstaltung);
        teilnehmerListeRepository.save(teilnehmerListe);

    }


    public void extractData(List<MultipartFile> multipartFiles, Long nutzerId ) throws IOException {
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
