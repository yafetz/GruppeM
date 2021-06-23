package Client.Modell;

public class ToDoItem {



    private Long id;

    private String titel;

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    private String deadline;

    private String verantwortliche;

    private boolean erledigt;
    private Long nutzerId;
    private Long projektgruppeId;

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

    public boolean getErledigt() {
        return erledigt;
    }

    public void setErledigt(boolean erledigt) {
        this.erledigt = erledigt;
    }

    public String getVerantwortliche() {
        return verantwortliche;
    }

    public void setVerantwortliche(String verantwortliche) {
        this.verantwortliche = verantwortliche;
    }





    public ToDoItem(){}
    public ToDoItem(String titel, String deadline, String verantwortliche) {

        this.titel= titel;
        this.deadline = deadline;

        this.verantwortliche=verantwortliche;
    }






}
