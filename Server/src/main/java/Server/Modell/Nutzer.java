package Server.Modell;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "nutzer")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Nutzer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String vorname;
    @Column(nullable = false)
    private String nachname;
    @Column(unique = true,nullable = false)
    private String email;
    @Column(nullable = false)
    private String passwort;
    @Column(nullable = true)
    private String profilbild;
    @Column(nullable = false)
    private String strasse;
    @Column(nullable = false)
    private int hausnummer;
    @Column(nullable = false)
    private int plz;
    @Column(nullable = false)
    private int stadt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        nachname = nachname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        email = email;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        passwort = passwort;
    }

    public String getProfilbild() {
        return profilbild;
    }

    public void setProfilbild(String profilbild) {
        profilbild = profilbild;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        strasse = strasse;
    }

    public int getHausnummer() {
        return hausnummer;
    }

    public void setHausnummer(int hausnummer) {
        hausnummer = hausnummer;
    }

    public int getPlz() {
        return plz;
    }

    public void setPlz(int plz) {
        this.plz = plz;
    }

    public int getStadt() {
        return stadt;
    }

    public void setStadt(int stadt) {
        this.stadt = stadt;
    }
}
