package Client.Controller;

import Client.Modell.Lehrender;
import Client.Modell.Lehrveranstaltung;
import Client.Modell.Student;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ResourceBundle;

public class LehrveranstaltungBeitreten implements Initializable {

    @FXML
    TextField lehrender;
    @FXML
    TextField semester;
    @FXML
    TextField lehrveranstaltungfield;
    @FXML
    Button beitreten;

    // Server import oder Client import???
    private Lehrveranstaltung lehrveranstaltung;
    private Student student;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void lehrveranstaltungBeitreten(){

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/beitreten/"+lehrveranstaltung.getId()+"&"+student.getId())).build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ;
    }

}
