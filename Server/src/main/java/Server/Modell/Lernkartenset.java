package Server.Modell;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "Lernkartenset")
public class Lernkartenset {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String bezeichnung;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "nutzer_Id", nullable = false)
    private Nutzer ersteller;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "projektgruppe_Id", nullable = false)
    private Projektgruppe projektgruppe;

    @Column(nullable = false)
    private boolean istGeteilt;

    public Lernkartenset(String bezeichnung, Nutzer ersteller, Projektgruppe projektgruppe, boolean istGeteilt) {
        this.bezeichnung = bezeichnung;
        this.ersteller = ersteller;
        this.projektgruppe = projektgruppe;
        this.istGeteilt = istGeteilt;
    }

    public Lernkartenset() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public Nutzer getErsteller() {
        return ersteller;
    }

    public void setErsteller(Nutzer ersteller) {
        this.ersteller = ersteller;
    }

    public Projektgruppe getProjektgruppe() {
        return projektgruppe;
    }

    public void setProjektgruppe(Projektgruppe projektgruppe) {
        this.projektgruppe = projektgruppe;
    }

    public boolean isIstGeteilt() {
        return istGeteilt;
    }

    public void setIstGeteilt(boolean istGeteilt) {
        this.istGeteilt = istGeteilt;
    }

    @Override
    public String toString() {
        return "Lernkartenset{" +
                "id=" + id +
                ", bezeichnung='" + bezeichnung + '\'' +
                ", ersteller=" + ersteller.getVorname() +
                ", projektgruppe=" + projektgruppe.getTitel() +
                ", istGeteilt=" + istGeteilt +
                '}';
    }
}
