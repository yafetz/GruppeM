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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="lernkartenset_Id", nullable = false)
    private Lernkartenset lernkartenset;

    @Column(nullable = false)
    @JsonProperty("frage")
    private String frage;

    @Column(nullable = false)
    @JsonProperty("antwort")
    private String antwort;

    public Lernkartenset getLernkartenset() {
        return lernkartenset;
    }

    public void setLernkartenset(Lernkartenset lernkartenset) {
        this.lernkartenset = lernkartenset;
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
