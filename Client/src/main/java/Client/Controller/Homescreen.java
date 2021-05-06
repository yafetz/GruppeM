package Client.Controller;

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
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class Homescreen {

    @FXML
    public Hyperlink namenLink;
    @FXML
    public ImageView profilBild;
    @FXML
    private Button meineKurse;
    @FXML
    private Button alleKurse;


    public void initialize() {

    }

    public void meineKurseAufrufen(ActionEvent event) {
        event.consume();
        Stage stage = (Stage) meineKurse.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("meineKurse.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            MeineKurse meineKurseKurse = loader.getController();
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
            AlleKurse alleKurse = loader.getController();
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
            Userprofil userprofil = loader.getController();
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

