package Client.Controller.Thema;

import Client.Layouts.Layout;
import Client.Modell.Student;
import Client.Modell.Thema;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class LiteraturHinzufügenController {

    @FXML
    public Label thema_titel;
    @FXML
    public TableView Literatur_noch_vorhanden_table;
    @FXML
    public TableColumn literatur_noch_vorhanden_titel_column;
    @FXML
    public Button addLiteratur;
    @FXML
    public TableView literatur_jetzt_table;
    @FXML
    public TableColumn literatur_jetzt_titel_column;
    @FXML
    public Button bibtex_button;

    public Layout layout;
    public Thema thema;

    public void setThema(Thema thema){
        this.thema = thema;
        thema_titel.setText(thema.getTitel());
        //Load Tables
    }

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public void LiteraturHinzufügen(ActionEvent actionEvent) {
    }

    public void BibtexFileUpload(ActionEvent actionEvent) {
        layout.instanceLayout("LiteraturUpload.fxml");
        ((LiteraturUploadController) layout.getController()).setLayout(layout);
        ((LiteraturUploadController) layout.getController()).setThema(thema);
    }

    public void populate_noch_vorhanden_literatur() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/projektgruppe/" + thema.getId() + "/Mitglieder")).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            List<Student> students = mapper.readValue(response.body(), new TypeReference<List<Student>>() {});

            literatur_noch_vorhanden_titel_column.setCellValueFactory(new PropertyValueFactory<Student, String>("NachnameVorname"));

            ObservableList<Student> obsStud = FXCollections.observableList(students);
            Literatur_noch_vorhanden_table.setItems(obsStud);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void populateNeueTableView() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/projektgruppe/" + thema.getId() + "/moeglicheMitglieder/")).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            List<Student> students = mapper.readValue(response.body(), new TypeReference<List<Student>>() {});

            literatur_jetzt_table.setEditable(true);
            literatur_jetzt_titel_column.setCellValueFactory(new PropertyValueFactory<Student, String>("NachnameVorname"));

            ObservableList<Student> obsStud = FXCollections.observableList(students);
            literatur_jetzt_table.setItems(obsStud);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
