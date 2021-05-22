package Client.Controller;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Client.Layouts.Layout;
import Client.Modell.Lehrender;
import Client.Modell.Lehrveranstaltung;
import Client.Modell.Nutzer;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
    private TextField semesters;

    @FXML
    private Button erstellen;

    @FXML
    private Button addCSV;
    @FXML
    private ChoiceBox<String> typVorlesung;

    private Object nutzerInstanz;

    ObservableList<String> options = FXCollections.observableArrayList("Vorlesung","Seminar");


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
        String split = tit.replaceAll(" ", "%20");

        System.out.println("Titel     " + split);




        String veranstaltungstyp = typVorlesung.getValue();
        System.out.println("Art:::     "+veranstaltungstyp);

        String sem = semesters.getText();
        String splitsem = sem.replaceAll(" ", "%20");
        System.out.println("Semester     " + sem);


        System.out.println("ID     " + nutzerId);


        Pattern pattern1 = Pattern.compile("sose");
        Pattern pattern2 = Pattern.compile("wise");
        Matcher matcher1 = pattern1.matcher(sem.toLowerCase());
        Matcher matcher2 = pattern2.matcher(sem.toLowerCase());
        boolean matchFound = matcher1.find();
        boolean matchFound2 = matcher2.find();
        if(matchFound || matchFound2) {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/create/lehrveranstaltung/"+split+"&"+nutzerId+"&"+veranstaltungstyp+"&"+splitsem)).POST(HttpRequest.BodyPublishers.noBody()).build();
            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println("Response body     "+ response.body());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("Falsches Semester");
        }




    }

        public Object getNutzerInstanz () {
            return nutzerInstanz;
        }
        public void setNutzerInstanz (Object nutzerInstanz){

        this.nutzerInstanz = nutzerInstanz;
        typVorlesung.setItems(options);

        }

    public void AddCsv(ActionEvent actionEvent) {
        Layout erstellenmitCSV = new Layout("lehrmaterialUpload.fxml",(Stage) addCSV.getScene().getWindow(),nutzerInstanz);
        if(erstellenmitCSV.getController() instanceof LehrmaterialController){
            ((LehrmaterialController) erstellenmitCSV.getController()).setNutzerInstanz(nutzerInstanz);
            ((LehrmaterialController) erstellenmitCSV.getController()).setModus("CSV");
        }
    }
}

