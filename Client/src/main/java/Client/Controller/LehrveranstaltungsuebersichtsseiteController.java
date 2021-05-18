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

    private Object lehrveranstaltung;
    private Object nutzer;


    public void initialize() {
    }

    @FXML
    private void materialUploadPressedButton(ActionEvent event) {
        Stage stage = (Stage) materialUpload.getScene().getWindow();
        Layout homeScreen = null;
            homeScreen = new Layout("lehrmaterialUpload.fxml", stage, nutzer);
            if (homeScreen.getController() instanceof LehrmaterialController) {
               ((LehrmaterialController) homeScreen.getController()).setNutzerInstanz(nutzer);
               ((LehrmaterialController) homeScreen.getController()).setLehrveranstaltung(lehrveranstaltung);
            }
    }

    public void uebersichtsseiteAufrufen(Object nutzer, Object lehrveranstaltung) {
        this.nutzer = nutzer;
        this.lehrveranstaltung= lehrveranstaltung;

        if (nutzer !=null) {
            if (nutzer instanceof Lehrender) {
                title.setText(((Lehrveranstaltung) lehrveranstaltung).getTitel());
                materialUpload.setText("Lehrmaterial hochladen");
            }
            else if(nutzer instanceof Student) {
                //title.setText(((Lehrveranstaltung) lehrveranstaltung).getTitel());
                materialUpload.setVisible(false);
            }

        }

    }
}