package Server.Modell;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "literaturthema")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class LiteraturThema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "thema_id", nullable = false)
    private Thema thema;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "literatur_id", nullable = false)
    private Literatur literatur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Thema getThema() {
        return thema;
    }

    public void setThema(Thema thema) {
        this.thema = thema;
    }

    public Literatur getLiteratur() {
        return literatur;
    }

    public void setLiteratur(Literatur literatur) {
        this.literatur = literatur;
    }

    @Override
    public String toString() {
        return "LiteraturThema{" +
                "id=" + id +
                ", thema=" + thema +
                ", literatur=" + literatur +
                '}';
    }
}
