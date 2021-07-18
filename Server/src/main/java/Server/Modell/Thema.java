package Server.Modell;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "thema")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Thema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @JsonProperty("titel")
    private String titel;
    @Column(nullable = false)
    @JsonProperty("beschreibung")
    private String beschreibung;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "lehrveranstaltungs_id", nullable = false)
    private Lehrveranstaltung lehrveranstaltung;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "lehrender_nutzer_id", nullable = false)
    private Nutzer nutzer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public Lehrveranstaltung getLehrveranstaltung() {
        return lehrveranstaltung;
    }

    public void setLehrveranstaltung(Lehrveranstaltung lehrveranstaltung) {
        this.lehrveranstaltung = lehrveranstaltung;
    }

    public Nutzer getNutzer() {
        return nutzer;
    }

    public void setNutzer(Nutzer nutzer) {
        this.nutzer = nutzer;
    }

    @Override
    public String toString() {
        return "Thema{" +
                "id=" + id +
                ", titel='" + titel + '\'' +
                ", beschreibung='" + beschreibung + '\'' +
                ", lehrveranstaltung=" + lehrveranstaltung +
                ", nutzer=" + nutzer +
                '}';
    }
}
