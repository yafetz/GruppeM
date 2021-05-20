package Client.Modell;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Lehrmaterial {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("lehrveranstaltung")
    private Lehrveranstaltung lehrveranstaltung;
    @JsonProperty("titel")
    private String titel;
    @JsonProperty("typ")
    private String typ;
    @JsonProperty("datei")
    private byte[] datei;

   

    public Long getId() {
        return id;
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

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public byte[] getDatei() {
        return datei;
    }

    public void setDatei(byte[] datei) {
        this.datei = datei;
    }

}
