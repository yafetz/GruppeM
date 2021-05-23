package Client.Modell;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Lehrmaterial {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("lehrveranstaltungs_id")
    private Lehrveranstaltung lehrveranstaltung;
    @JsonProperty("titel")
    private String titel;
    @JsonProperty("datei")
    private byte[] datei;

   

    public Long getId() {
        return id;
    }
    public void setId(Long id){
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

    public byte[] getDatei() {
        return datei;
    }

    public void setDatei(byte[] datei) {
        this.datei = datei;
    }

}
