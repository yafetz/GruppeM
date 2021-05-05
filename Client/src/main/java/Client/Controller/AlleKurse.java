package Client.Controller;


import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AlleKurse {

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




    public static void initialize() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().
                uri(URI.create("http://localhost:8080/lehrveranstaltung/all")).build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
