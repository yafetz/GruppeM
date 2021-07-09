package Client.Modell;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Review {
    @JsonProperty("id")
    private Long id;


    @JsonProperty("titel")
    private String titel;


    private Lehrveranstaltung lehrveranstaltung;


    @JsonProperty("lehrender")
    private Lehrender lehrender;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Lehrveranstaltung getLehrveranstaltung() {
        return lehrveranstaltung;
    }

    public void setLehrveranstaltung(Lehrveranstaltung lehrveranstaltung) {
        this.lehrveranstaltung = lehrveranstaltung;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public Lehrender getLehrender() {
        return lehrender;
    }

    public void setLehrender(Lehrender lehrender) {
        this.lehrender = lehrender;
    }


}
