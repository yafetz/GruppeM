package Client.Controller;


import Client.Modell.Lehrveranstaltung;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.ResourceBundle;

public class AlleKurseController implements Initializable {

    @FXML
    public ImageView profilBild;
    @FXML
    public Hyperlink namenLink;
    @FXML
    private TableView alleLv;
    @FXML
    private TableColumn col_LvTitel;
    @FXML
    private TableColumn col_LvSemester;
    @FXML
    private TableColumn col_LvArt;
    @FXML
    private TableColumn col_LvLehrende;
    @FXML
    private Button alleKurse;
    @FXML
    private Button meineKurse;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/lehrveranstaltung/all")).build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            //mapping data in response.body() to list of lehrveranstaltung-objects
            ObjectMapper mapper = new ObjectMapper();
            List<Lehrveranstaltung> lehrveranstaltungen = mapper.readValue(response.body(), new TypeReference<List<Lehrveranstaltung>>() {});

            col_LvTitel.setCellValueFactory(new PropertyValueFactory<Lehrveranstaltung,String>("Titel"));
            col_LvSemester.setCellValueFactory(new PropertyValueFactory<Lehrveranstaltung,String>("Semester"));
            col_LvArt.setCellValueFactory(new PropertyValueFactory<Lehrveranstaltung,String>("Art"));
            // ObservableList is required to populate the table alleLv using .setItems() :
            ObservableList<Lehrveranstaltung> obsLv = FXCollections.observableList(lehrveranstaltungen);
            alleLv.setItems(obsLv);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void meineKurseAufrufen(ActionEvent event) {
        event.consume();
        Stage stage = (Stage) meineKurse.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("meineKurse.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            MeineKurseController meineKurse = loader.getController();
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

    public void eigeneProfilSeiteAufrufen(ActionEvent event) {
        event.consume();
        Stage stage = (Stage) namenLink.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("userprofile.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            UserprofilController userprofil = loader.getController();
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
