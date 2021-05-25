package Server.Controller;

import Server.Modell.Lehrender;
import Server.Modell.Nutzer;
import Server.Modell.Student;
import Server.Repository.LehrenderRepository;
import Server.Repository.NutzerRepository;
import Server.Repository.StudentRepository;
import com.sun.xml.bind.v2.runtime.unmarshaller.IntData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/register")
public class RegistrierController /*()*/{
    private final StudentRepository studentRepository;
    private final NutzerRepository nutzerRepository;
    private final LehrenderRepository lehrenderRepository;


    @Autowired
    public RegistrierController(StudentRepository studentRepository, NutzerRepository nutzerRepository, LehrenderRepository lehrenderRepository) {
        this.studentRepository = studentRepository;
        this.nutzerRepository = nutzerRepository;
        this.lehrenderRepository = lehrenderRepository;
    }

    @PostMapping("/update/student")
    public String update_student(@RequestParam("nutzerId") long nutzerId,
                                 @RequestParam("passwort") String passwort,
                                 @RequestParam("studienfach") String studienfach,
                                 @RequestParam("hausnummer") int hausnummer,
                                 @RequestParam("plz") int plz,
                                 @RequestParam("stadt") String stadt,
                                 @RequestParam("strasse") String strasse,
                                 @RequestParam(value = "profilbild",required = false) MultipartFile profilbild) throws IOException {
        Nutzer updateNutzer = nutzerRepository.findNutzerById(nutzerId);
        Student updateStudent = studentRepository.findStudentByNutzerId(updateNutzer);
        updateNutzer.setPasswort(passwort);
        updateNutzer.setHausnummer(hausnummer);
        updateNutzer.setPlz(plz);
        updateNutzer.setStadt(stadt);
        updateNutzer.setStrasse(strasse);
        if(profilbild != null) {
            updateNutzer.setProfilbild(profilbild.getBytes());
        }
        updateStudent.setStudienfach(studienfach);
        updateStudent.setNutzerId(updateNutzer);
        studentRepository.save(updateStudent);
        return "OK";
    }

    @PostMapping("/update/lehrender")
    public String update_lehrender(@RequestParam("nutzerId") long nutzerId,
                                   @RequestParam("passwort") String passwort,
                                   @RequestParam("lehrstuhl") String lehrstuhl,
                                   @RequestParam("forschungsgebiet") String forschungsgebiet,
                                   @RequestParam("hausnummer") int hausnummer,
                                   @RequestParam("plz") int plz,
                                   @RequestParam("stadt") String stadt,
                                   @RequestParam("strasse") String strasse,
                                   @RequestParam(value = "profilbild",required = false) MultipartFile profilbild) throws IOException {
        Nutzer updateNutzer = nutzerRepository.findNutzerById(nutzerId);
        Lehrender updateLehrender = lehrenderRepository.findLehrenderByNutzerId(updateNutzer);
        updateNutzer.setPasswort(passwort);
        updateNutzer.setHausnummer(hausnummer);
        updateNutzer.setPlz(plz);
        updateNutzer.setStadt(stadt);
        updateNutzer.setStrasse(strasse);
        if(profilbild != null) {
            updateNutzer.setProfilbild(profilbild.getBytes());
        }
        updateLehrender.setLehrstuhl(lehrstuhl);
        updateLehrender.setForschungsgebiet(forschungsgebiet);
        updateLehrender.setNutzerId(updateNutzer);
        lehrenderRepository.save(updateLehrender);
        return "OK";
    }

    @PostMapping("/student")
    public String registriere_student(@RequestParam("vorname") String vorname,
                                    @RequestParam("nachname") String nachname,
                                    @RequestParam("email") String email,
                                    @RequestParam("passwort") String passwort,
                                    @RequestParam("studienfach") String studienfach,
                                    @RequestParam("hausnummer") int hausnummer,
                                    @RequestParam("plz") int plz,
                                    @RequestParam("stadt") String stadt,
                                    @RequestParam("strasse") String strasse,
                                    @RequestParam("rolle") String rolle,
                                    @RequestParam(value = "profilbild",required = false) MultipartFile profilbild) throws IOException {
        Nutzer nutzer = new Nutzer();
        nutzer.setEmail(email);
        nutzer.setHausnummer(hausnummer);
        nutzer.setNachname(nachname);
        nutzer.setVorname(vorname);
        nutzer.setPasswort(passwort);
        nutzer.setPlz(plz);
        nutzer.setStadt(stadt);
        nutzer.setStrasse(strasse);
        if(profilbild != null) {
            nutzer.setProfilbild(profilbild.getBytes());
        }
        nutzer.setRolle(rolle);
        nutzerRepository.save(nutzer);
        Student student = new Student();
        student.setMatrikelnummer(generiereMatrikelnummer());
        student.setStudienfach(studienfach);
        student.setNutzerId(nutzer);
        studentRepository.save(student);
        return "OK";
    }

    @PostMapping("/lehrender")
    public String registriere_lehrender(@RequestParam("vorname") String vorname,
                                      @RequestParam("nachname") String nachname,
                                      @RequestParam("email") String email,
                                      @RequestParam("passwort") String passwort,
                                      @RequestParam("lehrstuhl") String lehrstuhl,
                                      @RequestParam("forschungsgebiet") String forschungsgebiet,
                                      @RequestParam("hausnummer") int hausnummer,
                                      @RequestParam("plz") int plz,
                                      @RequestParam("stadt") String stadt,
                                      @RequestParam("strasse") String strasse,
                                      @RequestParam("rolle") String rolle,
                                      @RequestParam(value = "profilbild", required = false) MultipartFile profilbild) throws IOException {
        Nutzer nutzer = new Nutzer();
        nutzer.setEmail(email);
        nutzer.setHausnummer(hausnummer);
        nutzer.setNachname(nachname);
        nutzer.setVorname(vorname);
        nutzer.setPasswort(passwort);
        nutzer.setPlz(plz);
        nutzer.setStadt(stadt);
        nutzer.setStrasse(strasse);
        if(profilbild != null) {
            nutzer.setProfilbild(profilbild.getBytes());
        }
        nutzer.setRolle(rolle);
        nutzerRepository.save(nutzer);
        Lehrender lehrender = new Lehrender();
        lehrender.setLehrstuhl(lehrstuhl);
        lehrender.setForschungsgebiet(forschungsgebiet);
        lehrender.setNutzerId(nutzer);
        lehrenderRepository.save(lehrender);
        return "OK";
    }

    public int generiereMatrikelnummer() {
        int matr = 1000000;
        if (studentRepository.findTopByOrderByIdDesc() != null) {
            int id = Math.toIntExact(studentRepository.findTopByOrderByIdDesc().getId());
            return matr + id + 1;
        } else {
            return matr;
        }
    }


        public void Email() {
        String email = new String();
           // public static void main(String[] args){
                //    String email1 = "nachname.vorname@stud.uni-due.de";
                //  String email2 = "nachname.vornamestud.uni-due.de"; //invalide:false
               // System.out.println("email1= +EmailValidieren(EmailValidieren(Email1):
               //         System.out.println("email2= +EmailValidieren(EmailValidieren(Email2):
                if(!email.contains("@ && .")){
                    System.out.println("die Eingabe ist korrekt");
            }
                    if (email == null || email.isEmpty()) { //also leer oder falsch
                        System.out.println("Die Eingabe ist falsch!");
                         }         }


        public void Passwort () {

            IntData passwort = new IntData();
            if (passwort.length() >= 8) {
                System.out.println("Das Passwort ist gut!");
            }
                if (passwort == null || passwort.isEmpty() || passwort.length() < 8) {
                    System.out.println("Das Passwort ist zu kurz!");
                }

}}