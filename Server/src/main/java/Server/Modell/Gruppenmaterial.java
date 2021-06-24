package Server.Modell;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Arrays;

@Entity
@Table(name="gruppenmaterial")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Gruppenmaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@JsonProperty("id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "projektgruppen_id", nullable = false)
    //@JsonProperty("Projektgruppe")
    private Projektgruppe projektgruppe;

    @Column(nullable = false)
    //@JsonProperty("material_titel")
    private String material_titel;

    @Column(nullable = false)
    @Lob
    //@JsonProperty("datei")

    private byte[] datei;

    public Gruppenmaterial(Projektgruppe projektgruppe, String titel, byte[] datei) {
        this.projektgruppe = projektgruppe;
        this.material_titel = titel;
        this.datei = datei;
    }

    public Gruppenmaterial() {

    }

    public Long getId() {
        return id;
    }

    public Projektgruppe getProjektgruppe() {
        return projektgruppe;
    }

    public void setProjektgruppe(Projektgruppe projektgruppe) {
        this.projektgruppe = projektgruppe;
    }

    public String getMaterial_titel() {
        return material_titel;
    }

    public void setMaterial_titel(String material_titel) {
        this.material_titel = material_titel;
    }

    public byte[] getDatei() {
        return datei;
    }

    public void setDatei(byte[] datei) {
        this.datei = datei;
    }

    @Override
    public String toString() {
        return "Gruppenmaterial{" +
                "id=" + id +
                ", material_titel='" + material_titel + '\'' +
                ", datei=" + Arrays.toString(datei) +
                ", projektgruppe=" + projektgruppe +
                '}';
    }
}
