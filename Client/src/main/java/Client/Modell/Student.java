package Client.Modell;

import org.json.JSONObject;

public class Student {

    private int id;
    private int matrikelnummer;
    private String studienfach;
    private Nutzer nutzerId;

    public void addDataFromJson(JSONObject jsonObject){
        setId(jsonObject.getInt("id"));
        setMatrikelnummer(jsonObject.getInt("matrikelnummer"));
        setStudienfach(jsonObject.getString("studienfach"));
        Nutzer nutzer = new Nutzer();
        JSONObject jsonNutzer = (JSONObject) jsonObject.get("nutzerId");
        nutzer.addDataFromJson(jsonNutzer);
        setNutzerId(nutzer);
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


}
