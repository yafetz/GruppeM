package Client.Controller;

import Client.Layouts.Layout;
import Client.Modell.Lehrender;
import Client.Modell.Nutzer;
import Client.Modell.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;


public class HomescreenController {
  @FXML
    public Hyperlink namenLink;
    @FXML
    public ImageView profilBild;
    @FXML
    private Button meineKurse;
    @FXML
    private Button alleKurse;

    @FXML
    private Button createLehrveranstaltung;

    @FXML
    private Button students;
    private Object nutzerInstanz;


    public void initialize() {

    }

    public void studentenListe(ActionEvent event){
      Layout  studenten= new Layout("studentenListe.fxml", (Stage) students.getScene().getWindow(), nutzerInstanz);
      if(studenten.getController() instanceof StudentenListe){
        ((StudentenListe) studenten.getController()).setNutzerInstanz(nutzerInstanz);

      }
    }


    public void lehrveranstaltungErstellen(ActionEvent event){
      Layout lehrveranstaltungErstellen = new Layout("lehrveranstaltungErstellen.fxml", (Stage) createLehrveranstaltung.getScene().getWindow(),nutzerInstanz);
      if(lehrveranstaltungErstellen.getController() instanceof LehrveranstaltungErstellen){
        ((LehrveranstaltungErstellen) lehrveranstaltungErstellen.getController()).uebersichtsseiteAufrufen(nutzerInstanz);
        System.out.println("NUTZERINSTANZ     "+ nutzerInstanz);
      }
    }
    /*
    public void meineKurseAufrufen(ActionEvent event) {
        event.consume();
        Stage stage = (Stage) meineKurse.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("meineKurse.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            MeineKurseController meineKurseController = loader.getController();
            meineKurseController.setNutzerInstanz(nutzerInstanz);
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

    public void alleKurseAufrufen(ActionEvent event) {
        event.consume();
        Stage stage = (Stage) alleKurse.getScene().getWindow();
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

    public void eigeneProfilSeiteAufrufen(ActionEvent event) {
        event.consume();
        Stage stage = (Stage) namenLink.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("userprofile.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            UserprofilController userprofilController = loader.getController();
            userprofilController.nutzerprofilAufrufen(nutzerInstanz,nutzerInstanz);
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

   */ public Object getNutzerInstanz() {
        return nutzerInstanz;
    }

    public void setNutzerInstanz(Object nutzerInstanz) {
        this.nutzerInstanz = nutzerInstanz;
    }
}

