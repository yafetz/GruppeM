package Client.Controller;

import Client.Layouts.Layout;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class LehrveranstaltungsuebersichtsseiteController {
    @FXML
    private Label title;
    @FXML
    private Button materialUpload;
   // @FXML
    //private TableView test;


    private Object lehrveranstaltung;
    private Object nutzer;


    public void getMaterial() {
        System.out.println(" STart request");
        long id = 1;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/lehrmaterial/" +id)).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request,HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            System.out.println(response.body());
            List<Lehrmaterial> materials = mapper.readValue(response.body(), new TypeReference<List<Lehrmaterial>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(" End request");
    }
    public void initialize() {
        getMaterial();
        System.out.println("initialize erfolgreich");



    }




    @FXML
    private void materialUploadPressedButton(ActionEvent event) {
        Stage stage = (Stage) materialUpload.getScene().getWindow();
        Layout homeScreen = null;
            homeScreen = new Layout("lehrmaterialUpload.fxml", stage,nutzer);
            if (homeScreen.getController() instanceof HomescreenController) {
               // ((HomescreenController) homeScreen.getController()).setNutzerInstanz(nutzer);
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
        System.out.println("hello2325");


    }



}