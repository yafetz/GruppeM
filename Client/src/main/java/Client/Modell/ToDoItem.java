package Client.Modell;

import javafx.scene.control.DatePicker;

public class ToDoItem {
    private Long id;

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

    public boolean isErledigt() {
        return erledigt;
    }

    public void setErledigt(boolean erledigt) {
        this.erledigt = erledigt;
    }

    public Student getVerantwortlich() {
        return verantwortlich;
    }

    public void setVerantwortlich(Student verantwortlich) {
        this.verantwortlich = verantwortlich;
    }

    private String titel;
    private boolean erledigt;
    private Student verantwortlich;
    private DatePicker deadline;



    public ToDoItem(){}
    public ToDoItem (Long id, String titel, boolean erledigt, Student verantwortlich) {
        this.id= id;
        this.titel= titel;
        this.erledigt= erledigt;
        this.verantwortlich=verantwortlich;
    }






}
