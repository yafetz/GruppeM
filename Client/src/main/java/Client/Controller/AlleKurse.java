package Client.Controller;


import Client.Modell.Lehrveranstaltung;
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

public class AlleKurse implements Initializable {

    @FXML
    private TableView alleLv;
    @FXML
    public TableColumn col_LvTitel;
    @FXML
    public TableColumn col_LvSemester;
    @FXML
    public TableColumn col_LvArt;
    @FXML
    public TableColumn col_LvLehrende;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/lehrveranstaltung/all")).build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //mapping data in response.body() to JSON
            ObjectMapper mapper = new ObjectMapper();
            List<Lehrveranstaltung> lehrveranstaltungen = mapper.readValue(response.body(), new TypeReference<List<Lehrveranstaltung>>() {});

            col_LvTitel.setCellValueFactory(new PropertyValueFactory<Lehrveranstaltung,String>("Titel"));
            col_LvSemester.setCellValueFactory(new PropertyValueFactory<Lehrveranstaltung,String>("Semester"));
            col_LvArt.setCellValueFactory(new PropertyValueFactory<Lehrveranstaltung,String>("Art"));
            // to populate the table alleLv using .setItems() an ObservableList is required, hence next line
            ObservableList<Lehrveranstaltung> obsLv = FXCollections.observableList(lehrveranstaltungen);
            alleLv.setItems(obsLv);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
