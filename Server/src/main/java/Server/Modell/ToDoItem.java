package Server.Modell;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "termin")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ToDoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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


    @Column(nullable = false)
    private String titel;
    @Column(nullable = false)
    private boolean erledigt;




    public ToDoItem(){}
    public ToDoItem (Long id, String titel, boolean erledigt) {
        this.id= id;
        this.titel= titel;
        this.erledigt= erledigt;

    }







}
