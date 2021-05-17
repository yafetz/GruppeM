package Server.Modell;

import javax.persistence.*;

@Entity
@Table(name="lehrmaterial")
public class Lehrmaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "lehrveranstaltungs_Id", nullable = false)
    private Lehrveranstaltung lehrveranstaltung;

    @Column(nullable = false)
    private String titel;

    @Column(nullable = false)
    private String typ;

    @Column(nullable = false)
    @Lob
    private byte[] datei;

    public Lehrmaterial(Lehrveranstaltung lehrveranstaltung, String titel, String typ, byte[] datei) {
        this.lehrveranstaltung = lehrveranstaltung;
        this.titel = titel;
        this.typ = typ;
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

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public byte[] getDatei() {
        return datei;
    }

    public void setDatei(byte[] datei) {
        this.datei = datei;
    }
}
