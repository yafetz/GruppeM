package Client.Controller;

import Client.Layouts.Layout;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import Client.Controller.AlleKurseController;
import Client.Controller.MeineKurseController;
import Client.Modell.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LehrveranstaltungsuebersichtsseiteController {
    @FXML
    private Label title;
    @FXML
    private Button materialUpload;


    private Object nutzer;


    public void initialize() {

    }


    @FXML
    private void materialUploadPressedButton(ActionEvent event) {
        Stage stage = (Stage) materialUpload.getScene().getWindow();
        Layout homeScreen = null;
        try {
            homeScreen = new Layout("lehrmaterialUpload.fxml", stage);
            if (homeScreen.getController() instanceof HomescreenController) {
                ((HomescreenController) homeScreen.getController()).setNutzerInstanz(nutzer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}