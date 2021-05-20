package Client.Controller;

import Client.Modell.Lehrveranstaltung;
import Client.Modell.Nutzer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class TeilnehmerListeController {

    @FXML
    private TableView<Nutzer> teilnehmerTabelle;

    @FXML
    private TableColumn<Nutzer, String> Vorname;

    @FXML
    private TableColumn<Nutzer, String> Nachname;

    @FXML
    private TableColumn<Nutzer, String> Rolle;
    long id;
    private Object nutzerId;
    private Lehrveranstaltung lehrveranstaltung;


    public void populateTableView(){

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/teilnehmer/"+id)).build();
        HttpResponse<String> response;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());

            JSONArray jsonObject = new JSONArray(response.body());



           Vorname.setCellValueFactory(new PropertyValueFactory<>("vorname"));
           Nachname.setCellValueFactory(new PropertyValueFactory<>("nachname"));

            Rolle.setCellValueFactory(new PropertyValueFactory<>("rolle"));


            // ObjectMapper mapper = new ObjectMapper();
           // List<TeilnehmerListeController> nutzerList = mapper.readValue(response.body(), new TypeReference<List<TeilnehmerListeController>>() {});
            System.out.println("Nutzerliste      "+jsonObject.length());
            for(int i=0;i<jsonObject.length();i++){
                JSONObject nutzer= jsonObject.getJSONObject(i).getJSONObject("nutzerId");
                System.out.println(nutzer.get("vorname")+ " "+ nutzer.get("nachname"));
                Nutzer nutzer1 = new Nutzer();
                nutzer1.setVorname(nutzer.getString("vorname"));
                nutzer1.setNachname(nutzer.getString("nachname"));
                nutzer1.setRolle(nutzer.getString("rolle"));

                teilnehmerTabelle.getItems().add(nutzer1);
            }








        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }






    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Object getNutzer() {
        return nutzerId;
    }

    public void setNutzerInstanz(Object nutzer) {
        this.nutzerId = nutzer;
        System.out.println(id);
        populateTableView();

    }

    public Lehrveranstaltung getLehrveranstaltung() {
        return lehrveranstaltung;
    }

    public void setLehrveranstaltung(Lehrveranstaltung lehrveranstaltung) {
        this.lehrveranstaltung = lehrveranstaltung;
    }
}
