package Client.Modell;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
@JsonIgnoreProperties
public class Lehrveranstaltung {
    @JsonProperty("id")
    private int id;
    @JsonProperty("titel")
    private String titel;
    @JsonProperty("art")
    private String art;
    @JsonProperty("semester")
    private String semester;
    @JsonProperty("lehrender")
    private Lehrender lehrender;
    private int jahr;

    public int getJahr() {
        return jahr;
    }

    public void setJahr(int jahr) {
        this.jahr = jahr;
    }

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

    public Lehrender getLehrender() {
        return lehrender;
    }

    public void setLehrender(Lehrender lehrender) {
        this.lehrender = lehrender;
    }

//    used to display the lehrenderName in the tableviews
    public String getLehrenderName() {
        return lehrender.getLehrenderName();
    }

    @Override
    public String toString() {
        return "Lehrveranstaltung{" +
                "id=" + id +
                ", titel='" + titel + '\'' +
                ", art='" + art + '\'' +
                ", semester='" + semester + '\'' +
                ", lehrender=" + lehrender.getLehrenderName() +
                '}';
    }
}
