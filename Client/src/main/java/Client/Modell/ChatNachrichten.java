package Client.Modell;

public class ChatNachrichten {

    private Long id;
    private String nachrichten;
    private String datum;
    private Chat chat_id;
    private Nutzer nutzer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNachrichten() {
        return nachrichten;
    }

    public void setNachrichten(String nachrichten) {
        this.nachrichten = nachrichten;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public Nutzer getNutzer() {
        return nutzer;
    }

    public void setNutzer(Nutzer nutzer) {
        this.nutzer = nutzer;
    }
}
