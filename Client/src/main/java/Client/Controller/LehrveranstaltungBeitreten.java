package Client.Controller;

import Client.Modell.Lehrender;
import Client.Modell.Lehrveranstaltung;
import Client.Modell.Nutzer;
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


    private Lehrveranstaltung lehrveranstaltung;
    private Object nutzerInstanz;
    private long lehrveranstaltungsId;
    private long veranstaltung;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void lehrveranstaltungBeitreten(){

        long nutzerId = 0;
        HttpClient client = HttpClient.newHttpClient();
        if (nutzerInstanz instanceof Lehrender) {
            nutzerId = ((Lehrender) nutzerInstanz).getNutzerId().getId();
        }
        if (nutzerInstanz instanceof Student) {
            nutzerId = ((Student) nutzerInstanz).getNutzer().getId();
        }
        //long nutzerId = ((Nutzer) nutzerInstanz).getId();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/beitreten/"+lehrveranstaltungsId+"&"+nutzerId)).build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response);
            System.out.println(nutzerId);
            System.out.println("Veranstaltungid        "+ veranstaltung);

            System.out.println("LehrveranstaltungsId           "+lehrveranstaltungsId);
            System.out.println("Success");
            //System.out.println("Student instanz   "+((Student) nutzerInstanz).getId());
           // System.out.println("Lehrender instanz     "+((Lehrender) nutzerInstanz).getNutzerId().getId());




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

}
