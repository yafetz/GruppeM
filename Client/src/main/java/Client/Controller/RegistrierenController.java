package Client.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class RegistrierenController {
    @FXML
    private TextField email;
    @FXML
    private Button registrierbutton; //kommt hier nicht public?
    @FXML

private Button registrieren_lehrender;

@FXML
private Button registrieren_student;
@FXML
private Button zuruek;
@FXML
private Button registrieren;
@FXML
private TextField nachname;
@FXML
private TextField vorname;
@FXML
private TextField postleitzahl;
@FXML
private TextField stadt;
@FXML
private TextField strasse;
@FXML
private TextField gebäude_nummer;
@FXML
private PasswordField passwort;
@FXML
private TextField studienfach;
@FXML
private TextField lehrstuhl;
@FXML
private TextField forschungsgebiet;

@FXML
private CheckBox check_box;



    public void registrieren(ActionEvent Event){
        System.out.println(email.getText());
    }

    public void Rollenwechsel(ActionEvent actionEvent) {
        actionEvent.consume();
        Stage stage = (Stage) registrieren_lehrender.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("Registrieren_Lehrender.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(false);
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void Registrieren_Student(ActionEvent actionEvent) {
        actionEvent.consume();
        Stage stage = (Stage) registrieren_student.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("Registrieren_Student.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(false);
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        } }

    public void Zuruek(ActionEvent actionEvent) {
        actionEvent.consume();
        Stage stage = (Stage) zuruek.getScene().getWindow();

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("login.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(false);
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }  }

    public void Registrieren(ActionEvent actionEvent) {
        actionEvent.consume();
        if( registrieren_student==null){
            // als Student registrieren
        }
        else {
            // als Lehrender registrieren

        }
    }
}
/* public void lehrerregistrieren (ActionEvent Event){
System.out.println(registrierbuttonfürLehrende());
}
 */