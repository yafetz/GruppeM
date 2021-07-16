package Client.Modell;

public class ToDoItem {

    private Long id;
    private String titel;
    private String deadline;
    private String verantwortliche;
    private String erledigt;
    private Long nutzerId;
    private Long projektgruppeId;

    public ToDoItem(){}
    public ToDoItem(String titel, String deadline, String verantwortliche) {

        this.titel= titel;
        this.deadline = deadline;

        this.verantwortliche=verantwortliche;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getErledigt() {
        return erledigt;
    }

    public void setErledigt(String erledigt) {
        this.erledigt = erledigt;
    }

    public Long getNutzerId() {
        return nutzerId;
    }

    public void setNutzerId(Long nutzerId) {
        this.nutzerId = nutzerId;
    }

    public Long getProjektgruppeId() {
        return projektgruppeId;
    }

    public void setProjektgruppeId(Long projektgruppeId) {
        this.projektgruppeId = projektgruppeId;
    }

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

    public String getVerantwortliche() {
        return verantwortliche;
    }

    public void setVerantwortliche(String verantwortliche) {
        this.verantwortliche = verantwortliche;
    }

}
