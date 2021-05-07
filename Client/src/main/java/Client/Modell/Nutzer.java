package Client.Modell;

import org.json.JSONObject;

public class Nutzer {
    private int id;
    private String vorname;
    private String nachname;
    private String email;
    private String passwort;
    private String profilbild;
    private String strasse;
    private int hausnummer;
    private int plz;
    private String stadt;

    public void addDataFromJson(JSONObject jsonObject) {
        setId(jsonObject.getInt("id"));
        setVorname(jsonObject.getString("vorname"));
        setNachname(jsonObject.getString("nachname"));
        setEmail(jsonObject.getString("email"));
        setPasswort(jsonObject.getString("passwort"));
        setProfilbild("null");
        setStrasse(jsonObject.getString("strasse"));
        setHausnummer(jsonObject.getInt("hausnummer"));
        setPlz(jsonObject.getInt("plz"));
        //setStadt(jsonObject.getString("stadt"));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getProfilbild() {
        return profilbild;
    }

    public void setProfilbild(String profilbild) {
        this.profilbild = profilbild;
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

    public void setStadt(String stadt) {
        this.stadt = stadt;
    }
}
