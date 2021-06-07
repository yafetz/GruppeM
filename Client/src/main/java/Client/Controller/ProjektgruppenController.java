package Client.Controller;

import Client.Layouts.Layout;
import Client.Modell.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
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
import java.util.List;

public class ProjektgruppenController {
    @FXML
    public Label addStud_label;
    @FXML
    public ScrollPane scrollpane;
    @FXML
    private Label pglisteseitentitel_label;
    @FXML
    private Button pgErstellen_btn;
    @FXML
    private TextField suchen_txtfield;
    @FXML
    private TableView<Projektgruppe> pgListe_tableview;
    @FXML
    private TableColumn<Projektgruppe, String> pgTitel_col;
    @FXML
    private TableColumn<Projektgruppe, Integer> nrMitglieder_col;
    @FXML
    private TableColumn<Projektgruppe, Long> pgId_col;
    @FXML
    private Label beitretenPgTitel_label;
    @FXML
    private Label beitretenLvTitel_label;
    @FXML
    private Button beitreten_btn;
    @FXML
    private Button zurueck_btn;
    @FXML
    private Label erstellenLvTitel_label;
    @FXML
    private TextField pgTitel_txtfield;
    @FXML
    private TableView<Student> studentenliste_tableview;
    @FXML
    private TableColumn<Student, Boolean> checkbox_col;
    @FXML
    private TableColumn<Student, String> studentenname_col;
    @FXML
    private TableColumn<Student, Integer> matrnr_col;
    @FXML
    private Button erstellen_btn;


    private Object nutzer;
    private Lehrveranstaltung lehrveranstaltung;

    public void setPageTitel(String titel) {
        pglisteseitentitel_label.setText(titel);
    }

    public void setErstellenLvTitel_label(String lehrveranstaltungstitel) {
        erstellenLvTitel_label.setText("Lehrveranstaltung " + lehrveranstaltungstitel);
    }

    public Object getNutzer() {
        return nutzer;
    }

    public void setNutzer(Object nutzer) {
        this.nutzer = nutzer;
    }

    public Lehrveranstaltung getLehrveranstaltung() {
        return lehrveranstaltung;
    }

    public void setLehrveranstaltung(Lehrveranstaltung lehrveranstaltung) {
        this.lehrveranstaltung = lehrveranstaltung;
        populateTableView();
    }

    public void populateTableView() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/projektgruppe/lvId=" + lehrveranstaltung.getId())).build();
        java.net.http.HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            List<Projektgruppe> projektgruppen = mapper.readValue(response.body(), new TypeReference<List<Projektgruppe>>() {});

            pgTitel_col.setCellValueFactory(new PropertyValueFactory<Projektgruppe,String>("titel"));
            pgId_col.setCellValueFactory(new PropertyValueFactory<Projektgruppe,Long>("id"));

            pgTitel_col.setCellFactory(tablecell -> {
                TableCell<Projektgruppe, String> cell = new TableCell<Projektgruppe, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty) ;
                        setText(empty ? null : item);
                    }
                };
                cell.setCursor(Cursor.HAND);
                cell.setOnMouseClicked(e -> {
                            if (!cell.isEmpty()) {
                                redirectToProjektgruppeOverview(cell.getTableRow().getItem().getId());
                            }
                        }
                );
                return cell;
            });

            ObservableList<Projektgruppe> obsPG = FXCollections.observableList(projektgruppen);
            pgListe_tableview.setItems(obsPG);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void redirectToProjektgruppeOverview(Long id) {
        // TODO
    }

    // anklicken von "Neue Projektgruppe erstellen"-Button auf der Seite der Projektgruppenliste
    public void pgListeErstellenPressedButton(ActionEvent actionEvent) {
        actionEvent.consume();
        Layout neuePGerstellen = new Layout("projektgruppeErstellen.fxml", (Stage) pgErstellen_btn.getScene().getWindow(), nutzer);
        if (nutzer instanceof Lehrender) {
            if (neuePGerstellen.getController() instanceof ProjektgruppenController) {
                ((ProjektgruppenController) neuePGerstellen.getController()).setErstellenLvTitel_label(lehrveranstaltung.getTitel());
                ((ProjektgruppenController) neuePGerstellen.getController()).setNutzer(nutzer);
                ((ProjektgruppenController) neuePGerstellen.getController()).setLehrveranstaltung(lehrveranstaltung);
            }
        } else if (nutzer instanceof Student) {
            if (neuePGerstellen.getController() instanceof ProjektgruppenController) {
                ((ProjektgruppenController) neuePGerstellen.getController()).setErstellenLvTitel_label(lehrveranstaltung.getTitel());
                ((ProjektgruppenController) neuePGerstellen.getController()).addStud_label.setVisible(false);
                ((ProjektgruppenController) neuePGerstellen.getController()).studentenliste_tableview.setVisible(false);
                ((ProjektgruppenController) neuePGerstellen.getController()).scrollpane.setVisible(false);
                ((ProjektgruppenController) neuePGerstellen.getController()).setNutzer(nutzer);
                ((ProjektgruppenController) neuePGerstellen.getController()).setLehrveranstaltung(lehrveranstaltung);
            }
        }
    }

    public void beitretenPressedButton(ActionEvent actionEvent) {
        // TODO
    }

    // anklicken von "Zurück zur Projektgruppenliste"-Button auf der Projektgruppenbeitrittsseite
    public void zurueckPressedButton(ActionEvent actionEvent) {
        // TODO
    }

    // anklicken von "Projektgruppe erstellen"-Button auf der Seite zur Erstellung einer neuen Projektgruppe
    public void erstellenPressedButton(ActionEvent actionEvent) {
        actionEvent.consume();
        String pgTitel = pgTitel_txtfield.getText().trim();
        long nutzerID = -1;
        if (nutzer instanceof Lehrender) {
            nutzerID = ((Lehrender) nutzer).getNutzerId().getId();
        } else if (nutzer instanceof Student) {
            nutzerID = ((Student) nutzer).getNutzer().getId();
        }

        try(CloseableHttpClient client = HttpClients.createDefault()) {
            String url = "http://localhost:8080/projektgruppe/new";
            HttpPost post = new HttpPost(url);
            MultipartEntityBuilder entity = MultipartEntityBuilder.create();
            entity.setCharset(StandardCharsets.UTF_8);
            entity.addTextBody("titel", pgTitel, ContentType.create("text/plain", MIME.UTF8_CHARSET));
            entity.addTextBody("nutzer", String.valueOf(nutzerID), ContentType.create("text/plain", MIME.UTF8_CHARSET));
            entity.addTextBody("lehrveranstaltung", String.valueOf(lehrveranstaltung.getId()), ContentType.create("text/plain", MIME.UTF8_CHARSET));

            HttpEntity requestEntity = entity.build();
            post.setEntity(requestEntity);

            try (CloseableHttpResponse response = client.execute(post)) {
                HttpEntity responseEntity = response.getEntity();
                String responseBody = EntityUtils.toString(responseEntity);
                System.out.println(responseBody);

                if(responseBody.contentEquals("true")) {     // Projektgruppe wurde erstellt
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Projektgruppe erfolgreich erstellt!");
                    alert.setHeaderText("Ihre Projektgruppe wurde erfolgreich erstellt");
                    alert.setContentText("Zurück zur Projektgruppenliste");
                    alert.showAndWait();
                    Layout projektgruppenliste = new Layout("projektgruppenliste.fxml", (Stage) erstellen_btn.getScene().getWindow(), nutzer);
                    if (projektgruppenliste.getController() instanceof ProjektgruppenController) {
                        ((ProjektgruppenController) projektgruppenliste.getController()).setNutzer(nutzer);
                        ((ProjektgruppenController) projektgruppenliste.getController()).setLehrveranstaltung(lehrveranstaltung);
                        ((ProjektgruppenController) projektgruppenliste.getController()).setPageTitel("Projektgruppen der Lehrveranstaltung " + lehrveranstaltung.getTitel());
                    }
                } else {        // Projektgruppe existiert bereits mit diesem Titel
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erstellung nicht erfolgreich!");
                    alert.setHeaderText("Es ist bereits eine Projektgruppe mit diesem Titel vorhanden.");
                    alert.setContentText("Geben Sie einen anderen Titel ein");
                    alert.showAndWait();
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
