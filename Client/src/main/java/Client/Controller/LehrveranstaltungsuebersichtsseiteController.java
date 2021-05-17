package Client.Controller;

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

    public void initialize() {

    }



@FXML
    private void uploadPressedButton(ActionEvent event) {
        event.consume();
        Stage stage = (Stage) alle.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("alleKurse.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            AlleKurseController alleKurseController = loader.getController();
            alleKurseController.setNutzerInstanz(nutzerInstanz);
            Scene scene = new Scene(root);
            String homescreencss = getClass().getClassLoader().getResource("css/login.css").toExternalForm();
            scene.getStylesheets().add(homescreencss);
            stage.setScene(scene);
            stage.setMaximized(false);
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
