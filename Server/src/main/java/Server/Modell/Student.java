package Server.Modell;

import javax.persistence.*;

@Entity
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String Vorname;
    @Column(nullable = false)
    private String Nachname;
    @Column(unique = true,nullable = false)
    private String Email;
    @Column(unique = true,nullable = false, length = 7)
    private int Matrikelnummer;
    @Column(nullable = false)
    private String Passwort;
    @Column(unique = true,nullable = false)
    private String Profilbild;
    @Column(nullable = false)
    private String Strasse;
    @Column(nullable = false)
    private int Hausnummer;
    @Column(nullable = false)
    private int PLZ;
    @Column(nullable = false)
    private String Studienfach;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVorname() {
        return Vorname;
    }

    public void setVorname(String vorname) {
        Vorname = vorname;
    }

    public String getNachname() {
        return Nachname;
    }

    public void setNachname(String nachname) {
        Nachname = nachname;
    }

    public String getProfilbild() {
        return Profilbild;
    }

    public void setProfilbild(String profilbild) {
        Profilbild = profilbild;
    }

    public String getStrasse() {
        return Strasse;
    }

    public void setStrasse(String strasse) {
        Strasse = strasse;
    }

    public int getHausnummer() {
        return Hausnummer;
    }

    public void setHausnummer(int hausnummer) {
        Hausnummer = hausnummer;
    }

    public int getPLZ() {
        return PLZ;
    }

    public void setPLZ(int PLZ) {
        this.PLZ = PLZ;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public int getMatrikelnummer() {
        return Matrikelnummer;
    }

    public void setMatrikelnummer(int matrikelnummer) {
        Matrikelnummer = matrikelnummer;
    }

    public String getPasswort() {
        return Passwort;
    }

    public void setPasswort(String passwort) {
        Passwort = passwort;
    }

    public String getStudienfach() {
        return Studienfach;
    }

    public void setStudienfach(String studienfach) {
        Studienfach = studienfach;
    }

}
