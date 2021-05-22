package Client.Controller;

import Client.Layouts.Layout;
import Client.Modell.Lehrender;
import Client.Modell.Lehrveranstaltung;
import Client.Modell.Nutzer;
import Client.Modell.Student;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.ResourceBundle;

public class LehrveranstaltungBeitretenController implements Initializable {

    @FXML
    Label lehrender;
    @FXML
    Label semester;
    @FXML
    Label lehrveranstaltungfield;
    @FXML
    Button beitreten;


    private Lehrveranstaltung lehrveranstaltung;
    private Object nutzerInstanz;
    private long lehrveranstaltungsId;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void lehrveranstaltungBeitreten(){

        lehrveranstaltungsId=lehrveranstaltung.getId();
        long nutzerId = 0;
        HttpClient client = HttpClient.newHttpClient();
        if (nutzerInstanz instanceof Lehrender) {
            nutzerId = ((Lehrender) nutzerInstanz).getNutzerId().getId();
        }
        if (nutzerInstanz instanceof Student) {
            nutzerId = ((Student) nutzerInstanz).getNutzer().getId();
        }
        var values = new HashMap<String, Long>();
        values.put("nutzer_id",nutzerId);
        values.put("lehrveranstaltungsId",lehrveranstaltungsId);
        var objectMapper = new ObjectMapper();
        String requestBody=null;
        try {
            requestBody = objectMapper.writeValueAsString(values);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/beitreten/"+lehrveranstaltungsId+"&"+nutzerId)).POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response);
            System.out.println(nutzerId);


            System.out.println("LehrveranstaltungsId           "+lehrveranstaltungsId);
            System.out.println("NutzerId       "+nutzerId);
            System.out.println("Success");
            //System.out.println("Student instanz   "+((Student) nutzerInstanz).getId());
           // System.out.println("Lehrender instanz     "+((Lehrender) nutzerInstanz).getNutzerId().getId());

            Layout uebersicht= new Layout("lehrveranstaltungsuebersichtsseite.fxml",(Stage) beitreten.getScene().getWindow(),nutzerInstanz);
            if(uebersicht.getController() instanceof LehrveranstaltungsuebersichtsseiteController){
                ((LehrveranstaltungsuebersichtsseiteController) uebersicht.getController()).uebersichtsseiteAufrufen(nutzerInstanz, lehrveranstaltung);

            }


        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Hier");
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("Hier");
        }
        ;
    }
    public void  setLehrveranstaltungsId(long lehrveranstaltungsId){
        this.lehrveranstaltungsId=lehrveranstaltungsId;
    }
    public Object getNutzerInstanz() {
        return nutzerInstanz;
    }
    public void setNutzerInstanz(Object nutzerInstanz) {
        this.nutzerInstanz = nutzerInstanz;
    }
    public Lehrveranstaltung getLehrveranstaltung(){
        return lehrveranstaltung;
    }
    public void setLehrveranstaltung(Lehrveranstaltung lehrveranstaltung){
        this.lehrveranstaltung=lehrveranstaltung;
    }

}
