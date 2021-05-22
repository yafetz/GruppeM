package Client.Modell;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.json.JSONObject;
@JsonIgnoreProperties
public class Lehrender {

    private int id;
    private String lehrstuhl;
    private String forschungsgebiet;
    private Nutzer nutzerId;

    public void addDataFromJson(JSONObject jsonObject) {
        setId(jsonObject.getInt("id"));
        setLehrstuhl(jsonObject.getString("lehrstuhl"));
        setForschungsgebiet(jsonObject.getString("forschungsgebiet"));
        Nutzer nutzer = new Nutzer();
        JSONObject jsonNutzer = (JSONObject) jsonObject.get("nutzerId");
        nutzer.addDataFromJson(jsonNutzer);
        setNutzerId(nutzer);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLehrstuhl() {
        return lehrstuhl;
    }

    public void setLehrstuhl(String lehrstuhl) {
        this.lehrstuhl = lehrstuhl;
    }

    public String getForschungsgebiet() {
        return forschungsgebiet;
    }

    public void setForschungsgebiet(String forschungsgebiet) {
        this.forschungsgebiet = forschungsgebiet;
    }
    public String getVorname(){
        return nutzerId.getVorname();
    }
    public String getNachname(){
        return nutzerId.getNachname();
    }

    public Nutzer getNutzerId() {
        return nutzerId;
    }

    public void setNutzerId(Nutzer nutzerId) {
        this.nutzerId = nutzerId;
    }

//  used to display the lehrenderName in the tableviews
    public String getLehrenderName() {
        return nutzerId.getNachname() + ", " + nutzerId.getVorname();
    }
}
