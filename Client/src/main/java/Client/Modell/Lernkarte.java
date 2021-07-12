package Client.Modell;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Lernkarte {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("projektgruppe")
    private Long projektgruppe_id;

    @JsonProperty("frage")
    private String frage;

    @JsonProperty("antwort")
    private String antwort;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjektgruppe_id() {
        return projektgruppe_id;
    }

    public void setProjektgruppe_id(Long projektgruppe_id) {
        this.projektgruppe_id = projektgruppe_id;
    }

    public String getFrage() {
        return frage;
    }

    public void setFrage(String frage) {
        this.frage = frage;
    }

    public String getAntwort() {
        return antwort;
    }

    public void setAntwort(String antwort) {
        this.antwort = antwort;
    }
}
