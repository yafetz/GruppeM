package Client.Modell;

import org.json.JSONObject;

public class Lehrender {

    private int id;
    private String lehrstuhl;
    private String forschungsgebiet;
    private Nutzer nutzer;

    public void addDataFromJson(JSONObject jsonObject) {
        setId(jsonObject.getInt("id"));
        setLehrstuhl(jsonObject.getString("lehrstuhl"));
        setForschungsgebiet(jsonObject.getString("forschungsgebiet"));
        Nutzer nutzer = new Nutzer();
        JSONObject jsonNutzer = (JSONObject) jsonObject.get("nutzerId");
        nutzer.addDataFromJson(jsonNutzer);
        setNutzer(nutzer);
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

    public Nutzer getNutzer() {
        return nutzer;
    }

    public void setNutzer(Nutzer nutzer) {
        this.nutzer = nutzer;
    }
}
