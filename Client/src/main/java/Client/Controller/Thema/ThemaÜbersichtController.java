package Client.Controller.Thema;

import Client.Layouts.Layout;
import Client.Modell.Literatur;
import Client.Modell.Student;
import Client.Modell.TeilnehmerListe;
import Client.Modell.Thema;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ThemaÜbersichtController {
    @FXML
    public Button literaturHinzufuegen;
    @FXML
    public Label titel;
    @FXML
    public Label beschreibung;
    @FXML
    public VBox literaturliste;

    public Layout layout;
    public Thema thema;

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
        if(layout.getNutzer() instanceof Student){
            literaturHinzufuegen.setVisible(false);
        }
    }

    public void setThema(Thema thema){
        titel.setText(thema.getTitel());
        beschreibung.setText(thema.getBeschreibung());
        this.thema = thema;
        //Lade alle Literaturen
        LadeAlleLiteraturenZumThema();
    }

    public void LadeAlleLiteraturenZumThema(){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/themen/jetzigeLiteratur/" + thema.getId())).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            List<Literatur> alleLiteraturen = mapper.readValue(response.body(), new TypeReference<List<Literatur>>() {});
            for(Literatur literatur : alleLiteraturen) {
                Label kurs = new Label();
                kurs.setPadding(new Insets(10,10,10,10));
                kurs.setFont(new Font(20));
                kurs.setText(literatur.getTitle());
                kurs.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        //weiterleitung zu Literübersichtsseite
                        layout.instanceLayout("literaturübersichtsseite.fxml");
                        ((LiteraturÜbersichtController) layout.getController()).setLayout(layout);
                        ((LiteraturÜbersichtController) layout.getController()).setLiteratur(literatur);
                    }
                });
                literaturliste.getChildren().add(kurs);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void LiteraturHinzufuegen(ActionEvent actionEvent) {
        //Weiterleitung zu Literatur auswahl Seite
        layout.instanceLayout("LiteraturHinzufügen.fxml");
        ((LiteraturHinzufügenController) layout.getController()).setLayout(layout);
        ((LiteraturHinzufügenController) layout.getController()).setThema(thema);
    }
}
