package Server.Modell;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "Lernkarte")
public class Lernkarte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="projektgruppe")
    private Projektgruppe projektgruppe;

    @Column(nullable = false)
    @JsonProperty("frage")
    private String frage;

    @Column(nullable = false)
    @JsonProperty("antwort")
    private String antwort;

    public Projektgruppe getProjektgruppe() {
        return projektgruppe;
    }

    public void setProjektgruppe(Projektgruppe projektgruppe) {
        this.projektgruppe = projektgruppe;
    }

    public String getFrage() {
        return frage;
    }

    public void setFrage(String frage) {
        this.frage = frage;
    }

    public String getAntwort() {
        return antwort;
    }

    public void setAntwort(String antwort) {
        this.antwort = antwort;
    }
}
