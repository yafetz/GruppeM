package Client.Modell;

public class TeilnehmerListe {
    private Long id;
    private Lehrveranstaltung lehrveranstaltung;
    private Nutzer nutzerInstanz;


    public Long getid() {
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

    public Nutzer getNutzerInstanz() {
        return nutzerInstanz;
    }

    public void setNutzerInstanz(Nutzer nutzerInstanz) {
        this.nutzerInstanz = nutzerInstanz;
    }

}
