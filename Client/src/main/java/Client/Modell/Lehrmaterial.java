package Client.Modell;

public class Lehrmaterial {

    private int id;
    private String pfad;
    private String titel;
    private Lehrveranstaltung lehrveranstaltung;

    public Lehrmaterial(int id, String pfad, String titel, Lehrveranstaltung lehrveranstaltung) {
        this.id = id;
        this.pfad = pfad;
        this.titel = titel;
        this.lehrveranstaltung = lehrveranstaltung;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPfad() {
        return pfad;
    }

    public void setPfad(String pfad) {
        this.pfad = pfad;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public Lehrveranstaltung getLehrveranstaltung() {
        return lehrveranstaltung;
    }

    public void setLehrveranstaltung(Lehrveranstaltung lehrveranstaltung) {
        this.lehrveranstaltung = lehrveranstaltung;
    }
}
