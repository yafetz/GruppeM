package Client.Controller;

import Client.Modell.Lehrveranstaltung;
import Client.Modell.Nutzer;
import Client.Modell.TeilnehmerListe;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.ResourceBundle;

public class TeilnehmerListeController implements Initializable {
    @FXML
    private TableView teilnehmer;
    @FXML
    private TableColumn col_vorname;
    @FXML
    private TableColumn col_nachname;

    private Nutzer nutzerInstanz;



    public Lehrveranstaltung getLehrveranstaltung() {
        return lehrveranstaltung;
    }

    public void setLehrveranstaltung(Lehrveranstaltung lehrveranstaltung) {
        this.lehrveranstaltung = lehrveranstaltung;
    }

    private Lehrveranstaltung lehrveranstaltung;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void populateTableview(){
        long id = lehrveranstaltung.getId();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/teilnehmer/" +id)).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request,HttpResponse.BodyHandlers.ofString());

            //            mapping data in response.body() to a list of teilnehmerliste-objects
            ObjectMapper mapper = new ObjectMapper();
            System.out.println(response.body());
            List<TeilnehmerListe> teilnehmerListe = mapper.readValue(response.body(), new TypeReference<List<TeilnehmerListe>>() {});
            List<Nutzer> nutzers = null;
            List<String> vorname = null;
            List<String> nachname = null;

            for(TeilnehmerListe teilnehmer: teilnehmerListe){
                nutzers.add(teilnehmer.getNutzerId());
                vorname.add(teilnehmer.getNutzerId().getVorname());
                nachname.add(teilnehmer.getNutzerId().getNachname());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}



