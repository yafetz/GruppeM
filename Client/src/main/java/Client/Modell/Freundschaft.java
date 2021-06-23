package Client.Modell;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties
public class Freundschaft {
    private Long id;
    @JsonProperty("anfragender_nutzer")
    private Nutzer anfragender_nutzer;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Nutzer getAnfragender_nutzer() {
        return anfragender_nutzer;
    }

    public void setAnfragender_nutzer(Nutzer anfragender_nutzer) {
        this.anfragender_nutzer = anfragender_nutzer;
    }




}
