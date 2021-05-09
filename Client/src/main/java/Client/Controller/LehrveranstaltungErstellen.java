package Client.Controller;

import Client.Modell.Lehrender;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LehrveranstaltungErstellen {
    @FXML
    private TextField titel;

    @FXML
    private TextField typ;

    @FXML
    private TextField semester;

    @FXML
    private Button erstellen;

    private Lehrender lehrender;

    public void initialize() {

    }

    @FXML
    private void erstellenPressedButton(ActionEvent event) {
        event.consume();
        String tit = titel.getText();
        String type = typ.getText();
        String sem = semester.getText();
        int id = lehrender.getId();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/erstellen/lehrveranstaltung/"+tit+"&"+id+"&"+type+"&"+sem)).build();
        HttpResponse<String> response = null;
    }


}
