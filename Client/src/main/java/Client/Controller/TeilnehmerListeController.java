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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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

    public Lehrveranstaltung getLehrveranstaltung() {
        return lehrveranstaltung;
    }

    public void setLehrveranstaltung(Lehrveranstaltung lehrveranstaltung) {
        this.lehrveranstaltung = lehrveranstaltung;
    }

    private Lehrveranstaltung lehrveranstaltung;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        HttpClient client = HttpClient.newHttpClient();
        this.lehrveranstaltung=lehrveranstaltung;
        lehrveranstaltung.getId();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/teilnehmer/"+lehrveranstaltung.getId())).build();
        HttpResponse<String> response;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //mapping data in response.body() to JSON
            ObjectMapper mapper = new ObjectMapper();
            List<Nutzer> nutzers = mapper.readValue(response.body(), new TypeReference<List<Nutzer>>() {});
            col_vorname.setCellValueFactory(new PropertyValueFactory<Lehrveranstaltung,String>("Vorname"));
            col_nachname.setCellValueFactory(new PropertyValueFactory<Lehrveranstaltung,String>("Nachname"));

            // to populate the table alleLv using .setItems() an ObservableList is required, hence next line
            ObservableList<Nutzer> obsLv = FXCollections.observableList(nutzers);
            teilnehmer.setItems(obsLv);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
