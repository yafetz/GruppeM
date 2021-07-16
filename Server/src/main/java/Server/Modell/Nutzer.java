package Server.Modell;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    @Lob
    @JsonProperty("profilbild")
    private byte[] profilbild;
    @Column(nullable = false)
    private String strasse;
    @Column(nullable = false)
    private int hausnummer;
    @Column(nullable = false)
    private int plz;
    @Column(nullable = false)
    private String stadt;
    @Column(nullable = false)
    private String rolle;
    @Column(nullable = true)
    private String fa_code;

    public String getRolle() {
        return rolle;
    }

    public void setRolle(String rolle) {
        this.rolle = rolle;
    }

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
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public int getHausnummer() {
        return hausnummer;
    }

    public void setHausnummer(int hausnummer) {
        this.hausnummer = hausnummer;
    }

    public int getPlz() {
        return plz;
    }

    public void setPlz(int plz) {
        this.plz = plz;
    }

    public String getStadt() {
        return stadt;
    }

    public byte[] getProfilbild() {
        return profilbild;
    }

    public void setProfilbild(byte[] profilbild) {
        this.profilbild = profilbild;
    }

    public void setStadt(String stadt) {
        this.stadt = stadt;
    }

    public String getFa_code() {
        return fa_code;
    }

    public void setFa_code(String fa_code) {
        this.fa_code = fa_code;
    }


}
