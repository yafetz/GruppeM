package Client.Controller;

import Client.Modell.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Userprofil {
    @FXML
    private Label username;
    @FXML
    private Label mailadresse;
    @FXML
    private Label matr;

    private Student student;
    private Lehrender lehrender;



    public void initialize() {
    }
     public void setStudent(Student student) {
        this.student = student;
         username.setText(student.getNutzer().getVorname() + " " + student.getNutzer().getNachname());
         mailadresse.setText(student.getNutzer().getEmail());
         matr.setText(String.valueOf(student.getMatrikelnummer()));

     }
    public void setLehrender(Lehrender lehrender) {
        this.lehrender = lehrender;
        username.setText(lehrender.getNutzer().getVorname() + " " + lehrender.getNutzer().getNachname());
        mailadresse.setText(lehrender.getNutzer().getEmail());
        matr.setVisible(false);

    }



}
