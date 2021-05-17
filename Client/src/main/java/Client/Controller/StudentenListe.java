package Client.Controller;

import Client.Modell.Lehrveranstaltung;
import Client.Modell.Nutzer;
import Client.Modell.Student;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.ResourceBundle;

public class StudentenListe implements Initializable {

    @FXML
    private TableView<Student> tabelle;

    @FXML
    private TableColumn<Student, String> vorname;

    @FXML
    private TableColumn<Student, String> nachname;

    @FXML
    private Button profilbutton;

    private Object studentInstanz;
    private Student student;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void populateTableView(){
        System.out.println(studentInstanz);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/studenten/nutzer")).build();
        HttpResponse<String> response;


        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            //            mapping data in response.body() to a list of lehrveranstaltung-objects
            ObjectMapper mapper = new ObjectMapper();
            List<Student> studenten = mapper.readValue(response.body(), new TypeReference<List<Student>>() {});


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }






    }


    public void studentAufrufen(ActionEvent event){

    }
}
