package Client.Controller;

import Client.Modell.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class UserprofilController {
    @FXML
    private Label username;
    @FXML
    private Label mailadresse;
    @FXML
    private Label matr;

    private Student student;
    private Lehrender lehrender;

    private Object nutzerInstanz;



    public void initialize() {
    }
     public void setStudent(Student student) {
        this.student = student;
         username.setText(student.getNutzerId().getVorname() + " " + student.getNutzerId().getNachname());
         mailadresse.setText(student.getNutzerId().getEmail());
         matr.setText(String.valueOf(student.getMatrikelnummer()));

     }
    public void setLehrender(Lehrender lehrender) {
        this.lehrender = lehrender;
        username.setText(lehrender.getNutzerId().getVorname() + " " + lehrender.getNutzerId().getNachname());
        mailadresse.setText(lehrender.getNutzerId().getEmail());
        matr.setVisible(false);

    }

    public Object getNutzerInstanz() {
        return nutzerInstanz;
    }

    public void setNutzerInstanz(Object nutzerInstanz) {
        this.nutzerInstanz = nutzerInstanz;
    }
}
