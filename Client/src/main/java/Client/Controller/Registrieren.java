package Client.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.awt.event.ActionEvent;

public class Registrieren {
    @FXML
    private TextField email;
    @FXML
    private Button registrierbutton;
    @FXML
    public void registrieren (ActionEvent Event){
        System.out.println(email.getText());
    }



}
