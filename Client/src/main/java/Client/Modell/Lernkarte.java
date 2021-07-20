package Client.Modell;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Lernkarte {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("lernkartenset")
    private Lernkartenset lernkartenset;

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

    public Lernkartenset getLernkartenset() {
        return lernkartenset;
    }

    public void setLernkartenset(Lernkartenset lernkartenset) {
        this.lernkartenset = lernkartenset;
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
