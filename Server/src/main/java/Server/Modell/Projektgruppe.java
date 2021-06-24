package Server.Modell;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "projektgruppe")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Projektgruppe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@JsonProperty("id")
    private Long id;

    @Column(nullable = false)
    //@JsonProperty("titel")
    private String titel;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "lehrveranstaltungs_Id", nullable = false)
    //@JsonProperty("lehrveranstaltung")
    private Lehrveranstaltung lehrveranstaltung;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "chatraum", nullable = false)
    //@JsonProperty("chatRaum")
    private ChatRaum chatRaum;

    public Projektgruppe() {
    }

    public Projektgruppe(Lehrveranstaltung lehrveranstaltung, String titel) {
        this.lehrveranstaltung = lehrveranstaltung;
        this.titel = titel;
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

    public ChatRaum getChatRaum() {
        return chatRaum;
    }

    public void setChatRaum(ChatRaum chatRaum) {
        this.chatRaum = chatRaum;
    }

    @Override
    public String toString() {
        return "Projektgruppe{" +
                "id=" + id +
                ", lehrveranstaltung=" + lehrveranstaltung +
                ", titel='" + titel + '\'' +
                '}';
    }
}
