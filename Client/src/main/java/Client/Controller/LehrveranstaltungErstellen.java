package Client.Controller;

import Client.Modell.Lehrender;
import Client.Modell.Lehrveranstaltung;
import Client.Modell.Nutzer;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class LehrveranstaltungErstellen {
    @FXML
    private TextField titels;

    @FXML
    private TextField art;

    @FXML
    private TextField semesters;

    @FXML
    private Button erstellen;

    private Object nutzerInstanz;



    public void initialize() {

    }
    public void uebersichtsseiteAufrufen(Object nutzer) {
        this.nutzerInstanz = nutzer;

    }


    @FXML
    private void erstellenPressedButton(ActionEvent event) {
        event.consume();

        //Nutzer nutzer = ((Lehrender)nutzerInstanz).getNutzerId();
        long nutzerId = ((Lehrender) nutzerInstanz).getNutzerId().getId();
        //Nutzer nutzer = ((Lehrender) nutzerInstanz).getNutzerId();

        String tit = titels.getText();
        System.out.println("Titel     " + tit);

        String type = art.getText();
        System.out.println("typ     " + type);
        String sem = semesters.getText();
        System.out.println("Semester     " + sem);


        System.out.println("ID     " + nutzerId);




        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/create/lehrveranstaltung/"+tit+"&"+nutzerId+"&"+type+"&"+sem)).POST(HttpRequest.BodyPublishers.noBody()).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response body     "+ response.body());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

        public Object getNutzerInstanz () {
            return nutzerInstanz;
        }
        public void setNutzerInstanz (Object nutzerInstanz){
            this.nutzerInstanz = nutzerInstanz;
        }

    }

