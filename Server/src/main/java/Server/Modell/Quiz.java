package Server.Modell;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "quiz")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @Column(nullable = false)
    @JsonProperty("titel")
    private String titel;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "lehrveranstaltungs_Id", nullable = false)
    @JsonProperty("lehrveranstaltung")
    private Lehrveranstaltung lehrveranstaltung;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "lehrender_id", nullable = false)
    @JsonProperty("lehrender")
    private Lehrender lehrender;


    public Long getId() {
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

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public Lehrender getLehrender() {
        return lehrender;
    }

    public void setLehrender(Lehrender lehrender) {
        this.lehrender = lehrender;
    }
}
