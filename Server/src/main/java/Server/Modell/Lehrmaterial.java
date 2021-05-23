package Server.Modell;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name="lehrmaterial")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Lehrmaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "lehrveranstaltungs_Id", nullable = false)
    @JsonProperty("lehrveranstaltung")
    private Lehrveranstaltung lehrveranstaltung;

    @Column(nullable = false)
    @JsonProperty("titel")
    private String titel;

    @Column(nullable = false)
    @Lob
    @JsonProperty("datei")

    private byte[] datei;

    public Lehrmaterial(Lehrveranstaltung lehrveranstaltung, String titel, byte[] datei) {
        this.lehrveranstaltung = lehrveranstaltung;
        this.titel = titel;
        this.datei = datei;
    }

    public Lehrmaterial() {

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

    public byte[] getDatei() {
        return datei;
    }

    public void setDatei(byte[] datei) {
        this.datei = datei;
    }
}
