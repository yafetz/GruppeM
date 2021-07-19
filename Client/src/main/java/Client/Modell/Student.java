package Client.Modell;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import org.json.JSONObject;

public class Student {

    private int id;
    private int matrikelnummer;
    private String studienfach;
    private Nutzer nutzerId;
    private int versuche;
    private ObservableValue<Boolean> checked;

    public void addDataFromJson(JSONObject jsonObject){
        setId(jsonObject.getInt("id"));
        setMatrikelnummer(jsonObject.getInt("matrikelnummer"));
        setStudienfach(jsonObject.getString("studienfach"));
        Nutzer nutzer = new Nutzer();
        JSONObject jsonNutzer = (JSONObject) jsonObject.get("nutzerId");
        nutzer.addDataFromJson(jsonNutzer);
        setNutzerId(nutzer);
    }

    public Student() {
        this.checked = new SimpleBooleanProperty(false);
    }

    public int getId() {
        return id;
    }

    public String getVorname(){
        return nutzerId.getVorname();
    }
    public String getNachname(){
        return nutzerId.getNachname();
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getMatrikelnummer() {
        return matrikelnummer;
    }

    public void setMatrikelnummer(int matrikelnummer) {
        this.matrikelnummer = matrikelnummer;
    }

    public String getStudienfach() {
        return studienfach;
    }

    public void setStudienfach(String studienfach) {
        this.studienfach = studienfach;
    }

    public Nutzer getNutzer() {
        return nutzerId;
    }

    public void setNutzerId(Nutzer nutzer) {
        this.nutzerId = nutzer;
    }

    public String getStudentVorname() {
        return nutzerId.getVorname();
    }
    public String getStudentNachname() {
        return nutzerId.getNachname();
    }

    public String getNachnameVorname() {
        return nutzerId.getNachname() + ", " + nutzerId.getVorname();
    }

    public ObservableValue<Boolean> checkedProperty() {
        return checked;
    }
    public int getVersuche() {
        return versuche;
    }

    public void setVersuche(int versuche) {
        this.versuche = versuche;
    }
}
