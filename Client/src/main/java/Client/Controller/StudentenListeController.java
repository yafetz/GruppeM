package Client.Controller;

import Client.Layouts.Auth;
import Client.Layouts.Layout;
import Client.Modell.Lehrveranstaltung;
import Client.Modell.Nutzer;
import Client.Modell.Student;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.ResourceBundle;

public class StudentenListeController {

    @FXML
    private TableView<Student> tabelle;

    @FXML
    private TableColumn<Student, String> Vorname;

    @FXML
    private TableColumn<Student, String> Nachname;

    @FXML
    private TableColumn<Student, Integer> matrNr;

    @FXML
    private TableColumn<Student, Integer> studentenId;

    private Lehrveranstaltung lehrveranstaltung;
    private Object nutzerInstanz;



    public void initialize() {

    }

    public Lehrveranstaltung getLehrveranstaltung() {
        return lehrveranstaltung;
    }

    public void setLehrveranstaltung(Lehrveranstaltung lehrveranstaltung) {
        this.lehrveranstaltung = lehrveranstaltung;
        populateTableView();
    }

    public void setNutzerInstanz(Object nutzer) {
        this.nutzerInstanz = nutzer;
        System.out.println("NUtzerinstanz Stundentenliste   "+nutzerInstanz);
    }

    public void populateTableView(){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/teilnehmer/studentenliste/"+lehrveranstaltung.getId())).build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            ObjectMapper mapper = new ObjectMapper();
            List<Student> studenten = mapper.readValue(response.body(), new TypeReference<List<Student>>() {});
            Vorname.setCellValueFactory(new PropertyValueFactory<Student,String>("studentVorname"));
            Nachname.setCellValueFactory(new PropertyValueFactory<Student,String>("studentNachname"));
            matrNr.setCellValueFactory(new PropertyValueFactory<Student,Integer>("matrikelnummer"));

            Vorname.setCellFactory(tablecell -> {
                TableCell<Student, String> cell = new TableCell<Student, String>(){
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty) ;
                        setText(empty ? null : item);
                    }
                };
                cell.setCursor(Cursor.HAND);
                cell.setOnMouseClicked(e -> {
                            if (!cell.isEmpty()) {
                                zurLehrveranstaltungHinzufügen(cell.getTableRow().getItem().getId());
                            }
                        }
                );
                return cell;
            });

            Nachname.setCellFactory(tablecell -> {
                TableCell<Student, String> cell = new TableCell<Student, String>(){
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty) ;
                        setText(empty ? null : item);
                    }
                };
                cell.setCursor(Cursor.HAND);
                cell.setOnMouseClicked(e -> {
                            if (!cell.isEmpty()) {
                                zurLehrveranstaltungHinzufügen(cell.getTableRow().getItem().getId());
                            }
                        }
                );
                return cell;
            });
            matrNr.setCellFactory(tablecell -> {
                TableCell<Student, Integer> cell = new TableCell<Student, Integer>(){
                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty) ;
                        setText(empty ? null : String.valueOf(item));
                    }
                };
                cell.setCursor(Cursor.HAND);
                cell.setOnMouseClicked(e -> {
                            if (!cell.isEmpty()) {
                                zurLehrveranstaltungHinzufügen(cell.getTableRow().getItem().getId());
                            }
                        }
                );
                return cell;
            });
            ObservableList<Student> obsLv = FXCollections.observableList(studenten);
            tabelle.setItems(obsLv);
        } catch (IOException e) {
            System.out.println("ERROR HIER");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("ERROR DA");
            e.printStackTrace();
        }
    }

    private void zurLehrveranstaltungHinzufügen(int id){
        try (CloseableHttpClient client1 = HttpClients.createDefault()) {

            String url = "http://localhost:8080/teilnehmer/add";
            HttpPost post = new HttpPost(url);
            MultipartEntityBuilder entity = MultipartEntityBuilder.create();

            entity.addTextBody("studentId",String.valueOf(id));
            entity.addTextBody("lehrveranstaltungId",String.valueOf(lehrveranstaltung.getId()));

            HttpEntity requestEntity = entity.build();
            post.setEntity(requestEntity);

            try (CloseableHttpResponse response1 = client1.execute(post)) {
                HttpEntity responseEntity = response1.getEntity();
                String result = EntityUtils.toString(responseEntity);

                Layout teilnehmerListeView = new Layout("teilnehmerliste.fxml", (Stage) tabelle.getScene().getWindow(),nutzerInstanz);
                if(teilnehmerListeView.getController() instanceof TeilnehmerListeController){
                    long veranstaltungId = ((Lehrveranstaltung) lehrveranstaltung).getId();

                    ((TeilnehmerListeController) teilnehmerListeView.getController()).setId(veranstaltungId);
                    ((TeilnehmerListeController) teilnehmerListeView.getController()).setNutzerInstanz(nutzerInstanz);
                    ((TeilnehmerListeController)  teilnehmerListeView.getController()).setLehrveranstaltung(((Lehrveranstaltung) lehrveranstaltung));
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}