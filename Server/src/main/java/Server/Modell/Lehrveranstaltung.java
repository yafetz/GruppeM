package Server.Modell;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name ="lehrveranstaltung")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Lehrveranstaltung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String titel;
    @Column(nullable = false)
    private String art;
    @Column(nullable = false)
    private String semester;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "lehrender_Id", nullable = false)
    private Lehrender lehrender;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getArt() {
        return art;
    }

    public void setArt(String art) {
        this.art = art;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }


    public Lehrender getLehrender() {
        return lehrender;
    }

    public void setLehrender(Lehrender lehrender) {
        this.lehrender = lehrender;
    }

    @Override
    public String toString() {
        return "Lehrveranstaltung{" +
                "id=" + id +
                ", titel='" + titel + '\'' +
                ", art='" + art + '\'' +
                ", semester='" + semester + '\'' +
                ", lehrender=" + lehrender +
                '}';
    }
}
