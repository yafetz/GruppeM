package Client.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class RegistrierenController {
    @FXML
    private TextField email;
    @FXML
    private Button registrierbutton;


    public void registrieren(ActionEvent Event){
        System.out.println(email.getText());
    }
}
