package Client.Modell;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;

public class Nutzer {
    private int id;
    private String vorname;
    private String nachname;
    private String email;
    private String passwort;
    private byte[] profilbild;
    private String strasse;
    private int hausnummer;
    private int plz;
    private String stadt;
    private String rolle;
    private int fa_code;

    public int getVersuche() {
        return versuche;
    }

    public void setVersuche(int versuche) {
        this.versuche = versuche;
    }

    private int versuche;

    public void addDataFromJson(JSONObject jsonObject) {
        int id = jsonObject.getInt("id");
        setId(id);
        setVorname(jsonObject.getString("vorname"));
        setNachname(jsonObject.getString("nachname"));
        setEmail(jsonObject.getString("email"));
        setPasswort(jsonObject.getString("passwort"));
        setStrasse(jsonObject.getString("strasse"));
        setHausnummer(jsonObject.getInt("hausnummer"));
        setPlz(jsonObject.getInt("plz"));
        setStadt(jsonObject.getString("stadt"));
        setRolle("rolle");

        //profilbild auslesen und setten
        Connection connection= null;
        try {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sep","root","");
        PreparedStatement pstmt = connection.prepareStatement("select profilbild from nutzer WHERE id LIKE "+id);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        Blob datei = rs.getBlob("profilbild");
        setProfilbild(datei.getBinaryStream().readAllBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public int getFa_code() {
        return fa_code;
    }


    public void setId(int id) {
        this.id = id;
    }

    public String getVorname() {
        return vorname;
    }

    public String getRolle() {
        return rolle;
    }

    public void setRolle(String rolle) {
        this.rolle = rolle;
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

    public byte[] getProfilbild() {
        return profilbild;
    }

    public void setProfilbild(byte[] profilbild) {
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

    public String getName() {
        return vorname + " " + nachname;
    }
}
