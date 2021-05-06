package Client.Controller;

import Client.Modell.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class userprofil {
    @FXML
    private Label username;

    private Student student;
    private Lehrender lehrender;



    public void initialize() {
        if (student != null) {
            username.setText(student.getVorname() + " " + student.getNachname());
        }
        else if (lehrender != null) {

        }
    }



}
