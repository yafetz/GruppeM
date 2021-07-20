package Client.Controller.Thema;

import Client.Layouts.Layout;
import Client.Modell.Literatur;
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
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class LiteraturHinzufügenController {

    @FXML
    public Label thema_titel;
    @FXML
    public TableView<Literatur> Literatur_noch_vorhanden_table;
    @FXML
    public TableColumn<Literatur,String> literatur_noch_vorhanden_titel_column;
    @FXML
    public Button addLiteratur;
    @FXML
    public TableView<Literatur> literatur_jetzt_table;
    @FXML
    public TableColumn<Literatur,String> literatur_jetzt_titel_column;
    @FXML
    public Button bibtex_button;

    public Layout layout;
    public Thema thema;

    private List<Long> SelectedLiteraturIds = new ArrayList<>();

    public void setThema(Thema thema){
        this.thema = thema;
        thema_titel.setText(thema.getTitel());
        //Load Tables
        populate_noch_vorhanden_literatur();
        populate_jetzt_vorhanden_literatur();
    }

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public void LiteraturHinzufügen(ActionEvent actionEvent) {
        //Sende Daten an den Server
        try (CloseableHttpClient client1 = HttpClients.createDefault()) {

            String url = "http://localhost:8080/themen/addLiteraturZuThema";
            HttpPost post = new HttpPost(url);
            MultipartEntityBuilder entity = MultipartEntityBuilder.create();
            entity.setCharset(StandardCharsets.UTF_8);
            entity.addTextBody("thema_id",String.valueOf(thema.getId()), ContentType.create("text/plain", MIME.UTF8_CHARSET));
            for(int i = 0; i < SelectedLiteraturIds.size(); i++){
                entity.addTextBody("literaturliste",String.valueOf(SelectedLiteraturIds.get(i)));
            }
            HttpEntity requestEntity = entity.build();
            post.setEntity(requestEntity);

            try (CloseableHttpResponse response1 = client1.execute(post)) {
                HttpEntity responseEntity = response1.getEntity();
                String result = EntityUtils.toString(responseEntity);
                if(result != "") {
                    //Seite neuladen
                    layout.instanceLayout("LiteraturHinzufügen.fxml");
                    ((LiteraturHinzufügenController) layout.getController()).setLayout(layout);
                    ((LiteraturHinzufügenController) layout.getController()).setThema(thema);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void BibtexFileUpload(ActionEvent actionEvent) {
        layout.instanceLayout("LiteraturUpload.fxml");
        ((LiteraturUploadController) layout.getController()).setLayout(layout);
        ((LiteraturUploadController) layout.getController()).setThema(thema);
    }

    public void populate_noch_vorhanden_literatur() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/themen/alleNochVorhanden/" + thema.getId())).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            List<Literatur> alleLiteraturen = mapper.readValue(response.body(), new TypeReference<List<Literatur>>() {});

            literatur_noch_vorhanden_titel_column.setCellValueFactory(new PropertyValueFactory<Literatur, String>("Title"));

            ObservableList<Literatur> obsStud = FXCollections.observableList(alleLiteraturen);
            Literatur_noch_vorhanden_table.setItems(obsStud);
            addCheckBoxToTable(Literatur_noch_vorhanden_table);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void populate_jetzt_vorhanden_literatur() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/themen/jetzigeLiteratur/" + thema.getId())).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            List<Literatur> alleLiteraturen = mapper.readValue(response.body(), new TypeReference<List<Literatur>>() {});

            literatur_jetzt_titel_column.setCellValueFactory(new PropertyValueFactory<Literatur, String>("Title"));

            ObservableList<Literatur> obsStud = FXCollections.observableList(alleLiteraturen);
            literatur_jetzt_table.setItems(obsStud);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void addCheckBoxToTable(TableView table) {
        TableColumn<Literatur, Void> colBtn = new TableColumn("Ausgewählt");
        Callback<TableColumn<Literatur, Void>, TableCell<Literatur, Void>> cellFactory = new Callback<TableColumn<Literatur, Void>, TableCell<Literatur, Void>>() {
            @Override
            public TableCell<Literatur, Void> call(final TableColumn<Literatur, Void> param) {
                final TableCell<Literatur, Void> cell = new TableCell<Literatur, Void>() {
                    private final CheckBox checkBox = new CheckBox();
                    {
                        checkBox.setOnAction((ActionEvent event) -> {
                            Literatur literatur = getTableView().getItems().get(getIndex());
                            if (checkBox.isSelected()) {        // wenn Häckchen gesetzt wird, füge Student zu der Liste der ausgewählten Studenten hinzu
                                SelectedLiteraturIds.add((long)literatur.getId());
                            } else {                            // wenn Häckchen entfernt wird, entferne den Studenten von der Liste
                                for (int i = 0; i < SelectedLiteraturIds.size(); i++) {
                                    if (SelectedLiteraturIds.get(i) == literatur.getId()) {
                                        SelectedLiteraturIds.remove(i);
                                    }
                                }
                            }
                        });
                    }
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(checkBox);
                        }
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);
        table.getColumns().add(colBtn);
    }

}
