package Client.Controller;

import Client.Modell.Lehrveranstaltung;
import Client.Modell.Nutzer;
import Client.Modell.Student;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;


import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.ResourceBundle;

public class StudentenListe {

    @FXML
    private TableView<Student> tabelle;

    @FXML
    private TableColumn<Student, String> Vorname;

    @FXML
    private TableColumn<Student, String> Nachname;

    @FXML
    private TableColumn<Student, Integer> matrNr;

    @FXML
    private Button profilbutton;

    private Object nutzerInstanz;



    public void initialize() {

    }
    public void setNutzerInstanz(Object nutzer) {
        this.nutzerInstanz = nutzer;
        System.out.println("NUtzerinstanz Stundentenliste   "+nutzerInstanz);
        populateTableView();

    }

    public void populateTableView(){

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/studenten/all")).build();
        HttpResponse<String> response;


        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            //            mapping data in response.body() to a list of lehrveranstaltung-objects
            ObjectMapper mapper = new ObjectMapper();
            List<Student> studenten = mapper.readValue(response.body(), new TypeReference<List<Student>>() {});




            Vorname.setCellValueFactory(new PropertyValueFactory<Student,String>("studentVorname"));
            Nachname.setCellValueFactory(new PropertyValueFactory<Student,String>("studentNachname"));
            matrNr.setCellValueFactory(new PropertyValueFactory<Student,Integer>("matrikelnummer"));





            ObservableList<Student> obsLv = FXCollections.observableList(studenten);
            tabelle.setItems(obsLv);






        } catch (IOException e) {
            System.out.println("ERROR HIER");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("ERROR DA");
            e.printStackTrace();
        }






    }



}
