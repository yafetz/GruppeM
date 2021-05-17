package Client.Modell;

public class TeilnehmerListe {
    private Long id;
    private Lehrveranstaltung lehrveranstaltung;
    private Student studentId;


    public Long getid() {
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
