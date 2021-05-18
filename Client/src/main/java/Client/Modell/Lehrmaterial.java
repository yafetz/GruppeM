package Client.Modell;

public class Lehrmaterial {

    private Long id;
    private Lehrveranstaltung lehrveranstaltung;
    private String titel;
    private String typ;
    private byte[] datei;

    public Lehrmaterial(Lehrveranstaltung lehrveranstaltung, String titel, String typ, byte[] datei) {
        this.lehrveranstaltung = lehrveranstaltung;
        this.titel = titel;
        this.typ = typ;
        this.datei = datei;
    }

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
