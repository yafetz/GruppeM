package Client.Modell;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Gruppenmaterial {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("projektgruppe")
    private Projektgruppe projektgruppe;
    @JsonProperty("material_titel")
    private String titel;
    @JsonProperty("datei")
    private byte[] datei;



    public Long getId() {
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }

    public Projektgruppe getprojektgruppe() {
        return projektgruppe;
    }

    public void setprojektgruppe(Projektgruppe projektgruppe) {
        this.projektgruppe = projektgruppe;
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
