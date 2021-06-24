package Server.Modell;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "toDoItem")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ToDoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String titel;
    @Column(nullable = false)
    private String deadline;
    @Column(nullable = false)
    private String verantwortliche;

    public String getErledigt() {
        return erledigt;
    }

    public void setErledigt(String erledigt) {
        this.erledigt = erledigt;
    }

    @Column(nullable = false)
    private String erledigt;
    @Column(nullable = false)
    private Long projektgruppeId;

    public Long getNutzerId() {
        return nutzerId;
    }

    public void setNutzerId(Long nutzerId) {
        this.nutzerId = nutzerId;
    }

    @Column(nullable = false)
    private Long nutzerId;

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







    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getVerantwortliche() {
        return verantwortliche;
    }

    public void setVerantwortliche(String verantwortliche) {
        this.verantwortliche = verantwortliche;
    }






    public ToDoItem(){}
    public ToDoItem (String deadline, String titel,String verantwortliche) {
        this.deadline= deadline;
        this.titel= titel;
        this.verantwortliche= verantwortliche;

    }



}
