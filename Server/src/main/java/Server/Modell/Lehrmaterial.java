package Server.Modell;

import javax.persistence.*;

@Entity
@Table(name="lehrmaterial")
public class Lehrmaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "lehrveranstaltung_id", nullable = false)
    private Lehrveranstaltung lehrveranstaltung;
    @Column(nullable = false)
    private String pfad;
    @Column(unique = true, nullable = false)
    private String name;

    private byte[] inhalt;

    public Lehrveranstaltung getLehrveranstaltung() {
        return lehrveranstaltung;
    }

    public void setLehrveranstaltung(Lehrveranstaltung lehrveranstaltung) {
        this.lehrveranstaltung = lehrveranstaltung;
    }

    public String getPfad() {
        return pfad;
    }

    public void setPfad(String pfad) {
        this.pfad = pfad;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getInhalt() {
        return inhalt;
    }

    public void setInhalt(byte[] inhalt) {
        this.inhalt = inhalt;
    }
}
