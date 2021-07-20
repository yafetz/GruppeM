package Client.Modell;

public class Lernkartenset {

    private long id;
    private String bezeichnung;
    private Nutzer ersteller;
    private Projektgruppe projektgruppe;
    private boolean istGeteilt;

    public Lernkartenset(long id, String bezeichnung, Nutzer ersteller, Projektgruppe projektgruppe, boolean istGeteilt) {
        this.id = id;
        this.bezeichnung = bezeichnung;
        this.ersteller = ersteller;
        this.projektgruppe = projektgruppe;
        this.istGeteilt = istGeteilt;
    }

    public Lernkartenset() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public Nutzer getErsteller() {
        return ersteller;
    }

    public void setErsteller(Nutzer ersteller) {
        this.ersteller = ersteller;
    }

    public Projektgruppe getProjektgruppe() {
        return projektgruppe;
    }

    public void setProjektgruppe(Projektgruppe projektgruppe) {
        this.projektgruppe = projektgruppe;
    }

    public String getErstellerVorname() {
        return ersteller.getVorname();
    }

    public boolean isIstGeteilt() {
        return istGeteilt;
    }

    public void setIstGeteilt(boolean istGeteilt) {
        this.istGeteilt = istGeteilt;
    }
}
