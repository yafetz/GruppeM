package Client.Modell;

public class Lehrveranstaltung {

    private int id;
    private String titel;
    private String art;
    private String semester;
//    private Lehrender lehrende;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getArt() {
        return art;
    }

    public void setArt(String art) {
        this.art = art;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

//    public Client.Modell.Lehrender getLehrende() {
//        return lehrende;
//    }
//
//    public void setLehrende(Client.Modell.Lehrender lehrende) {
//        this.lehrende = lehrende;
//    }

    @Override
    public String toString() {
        return "Lehrveranstaltung{" +
                "titel='" + titel + '\'' +
                '}';
    }
}
