package Client.Modell;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Gruppenmitglied {
    private int id;
    private Lehrveranstaltung lehrveranstaltung;
    private Student student;

    public Projektgruppe getProjektgruppe() {
        return projektgruppe;
    }

    public void setProjektgruppe(Projektgruppe projektgruppe) {
        this.projektgruppe = projektgruppe;
    }

    private Projektgruppe projektgruppe;




    public Integer getId() {
        return id;
    }

    private final StringProperty vorname = new SimpleStringProperty();

    public final StringProperty vornameProperty() {
        return vorname;
    }

    public final String getVorname() {
        return vorname.get();
    }

    public final void setVorname(String value) {
        vorname.set(value);
    }

    private final StringProperty nachname = new SimpleStringProperty();

    public final StringProperty nachnameProperty() {
        return nachname;
    }

    public final String getNachname() {
        return nachname.get();
    }

    public final void setNachname(String value) {
        nachname.set(value);
    }

    public void setId(int id) {
        this.id = id;
    }

    public Lehrveranstaltung getLehrveranstaltung() {
        return lehrveranstaltung;
    }

    public void setLehrveranstaltung(Lehrveranstaltung lehrveranstaltung) {
        this.lehrveranstaltung = lehrveranstaltung;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
