package Client.Modell;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties
public class Freundschaft {
    private Long id;
    private Nutzer anfragender_nutzer;
    private Nutzer angefragter_nutzer;
    private Chat chat;
    private boolean status;

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

    public Nutzer getAngefragter_nutzer() {
        return angefragter_nutzer;
    }

    public void setAngefragter_nutzer(Nutzer angefragter_nutzer) {
        this.angefragter_nutzer = angefragter_nutzer;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getAnfragender_nutzer_nachname() {
        return anfragender_nutzer.getNachname();
    }

    public String getAngefragter_nutzer_nachname() {
        return angefragter_nutzer.getNachname();
    }

    public String getAnfragender_nutzer_vorname() {
        return anfragender_nutzer.getVorname();
    }

    public String getAngefragter_nutzer_vorname() {
        return angefragter_nutzer.getVorname();
    }

}
