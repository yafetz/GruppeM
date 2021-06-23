package Server.Modell;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "gruppenmitglied")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Gruppenmitglied {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "student_Id", nullable = false)
    private Student student;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "projektgruppe_Id", nullable = false)
    private Projektgruppe projektgruppe;


    public Gruppenmitglied(Student student, Projektgruppe projektgruppe) {
        this.student = student;
        this.projektgruppe = projektgruppe;
    }
    public Gruppenmitglied() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Projektgruppe getProjektgruppe() {
        return projektgruppe;
    }

    public void setProjektgruppe(Projektgruppe projektgruppe) {
        this.projektgruppe = projektgruppe;
    }
}
