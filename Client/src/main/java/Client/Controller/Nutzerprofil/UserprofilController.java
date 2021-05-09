package Client.Controller.Nutzerprofil;

import Client.Controller.AlleKurseController;
import Client.Modell.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class UserprofilController {
    @FXML
    private Label username;
    @FXML
    private Label mailadresse;
    @FXML
    private Label matr;
    @FXML
    private Label plz;
    @FXML
    private Label adresse;
    @FXML
    private Label lehrstuhl;
    @FXML
    private Label forschungsgebiet;
    @FXML
    private Label city;
    @FXML
    private Label number;
    @FXML
    private Button allCourses;
    @FXML
    private Button myCourses;
    @FXML
    private Label forschung;
    @FXML
    private Label teaching;

    private Student student;
    private Lehrender lehrender;



    public void initialize() {
    }
     public void setStudent(Student student) {
        this.student = student;
         username.setText(student.getNutzer().getVorname() + " " + student.getNutzer().getNachname());
         mailadresse.setText(student.getNutzer().getEmail());
         matr.setText(String.valueOf(student.getMatrikelnummer()));
         plz.setText(String.valueOf(student.getNutzer().getPlz()));
         adresse.setText(student.getNutzer().getStrasse());
         number.setText(String.valueOf(student.getNutzer().getHausnummer()));
         lehrstuhl.setVisible(false);
         forschungsgebiet.setVisible(false);
         forschung.setVisible(false);
         teaching.setVisible(false);

     }
    public void setLehrender(Lehrender lehrender) {
        this.lehrender = lehrender;
        username.setText(lehrender.getNutzer().getVorname() + " " + lehrender.getNutzer().getNachname());
        mailadresse.setText(lehrender.getNutzer().getEmail());
        matr.setVisible(false);
        plz.setText(String.valueOf(lehrender.getNutzer().getPlz()));
        adresse.setText(lehrender.getNutzer().getStrasse());
        number.setText(String.valueOf(lehrender.getNutzer().getHausnummer()));
        lehrstuhl.setText(lehrender.getLehrstuhl());
        forschungsgebiet.setText(lehrender.getForschungsgebiet());

    }


    public void meineKurseAufrufen(ActionEvent actionEvent) {


    }

    public void alleKurseAufrufen(ActionEvent actionEvent) {
        actionEvent.consume();
        Stage stage = (Stage) allCourses.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("alleKurse.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            AlleKurseController alleKurse = loader.getController();
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
}
