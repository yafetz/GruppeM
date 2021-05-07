package Server.Modell;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "teilnehmerliste")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TeilnehmerListe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "lehrveranstaltungsId", nullable = false)
    private Lehrveranstaltung lehrveranstaltung;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="studentId", nullable=true)
    private Student studentId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Lehrveranstaltung getLehrveranstaltung() {
        return lehrveranstaltung;
    }

    public void setLehrveranstaltung(Lehrveranstaltung lehrveranstaltung) {
        this.lehrveranstaltung = lehrveranstaltung;
    }

    public Student getStudentId() {
        return studentId;
    }

    public void setStudentId(Student studentId) {
        this.studentId = studentId;
    }
}
