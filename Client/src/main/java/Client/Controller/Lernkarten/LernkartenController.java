package Client.Controller.Lernkarten;

import Client.Controller.ProjektGruppe.ProjektgruppeBeitretenController;
import Client.Controller.ProjektGruppe.ProjektgruppenController;
import Client.Layouts.Layout;
import Client.Modell.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
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
import java.util.Collections;
import java.util.List;

public class LernkartenController {

    @FXML public Label lernkartensetsPgTitel;
    @FXML public Label lernkartensetsLvTitel;
    @FXML private Button neuerLernkartenset;
    @FXML private TableView<Lernkartenset> eigeneLernkartensets;
    @FXML private TableColumn<Lernkartenset, String> lernkartensetColumn;
    @FXML private TableColumn<Lernkartenset, Integer> eigeneSetsId;
    @FXML private TableView<Lernkartenset> geteilteLernkartensets;
    @FXML private TableColumn<Lernkartenset, String> geteilteLernkartensetsColumn;
    @FXML private TableColumn<Lernkartenset, String> erstellerColumn;
    @FXML private TableColumn<Lernkartenset, Integer> geteilteSetsId;
    @FXML private Button teilenButton;
    @FXML private Label pgTitel;
    @FXML private Label lvTitel;
    @FXML private TableView<Lernkartenset> geteilteSets;
    @FXML private TableColumn<Lernkartenset, String> geteilteSetsColumn;
    @FXML private TableView<Lernkartenset> ungeteilteSets;
    @FXML private TableColumn<Lernkartenset, String> ungeteilteSetsColumn;
    @FXML private TableColumn<Lernkartenset, Integer> setId;
    @FXML private Button setTeilenButton;
    @FXML private Button teilenZurueckButton;
    @FXML private Label setErstellenPgTitel;
    @FXML private Label setErstellenLvTitel;
    @FXML private TextField bezTextfield;
    @FXML private Button abbrechenButton;
    @FXML private Button erstellenButton;


    @FXML
    private Label frage;
    @FXML
    private Label antwort;
    @FXML
    private Button back;
    @FXML
    private Button previous;
    @FXML
    private Button lösung;
    @FXML
    private Button next;
    @FXML
    private Button create;
    @FXML
    public Label lernkartenanzahl;


    private Layout layout;
    private Object nutzer;
    private Lehrveranstaltung lehrveranstaltung;
    private Projektgruppe projektgruppe;
    private Lernkartenset lernkartenset;
    private List<Long> selectedSetsIds = new ArrayList<>();


    private List<Lernkarte> lernkartenList;

    private Lernkarte currentLernkarte;

    int currentPositionInLernkartenList;

    boolean showLösung;


    public void initLernkartenController(long lernkartensetId) {

        currentPositionInLernkartenList = 0;
        showLösung = false;
        frage.setText("");
        antwort.setText("");
        lernkartenanzahl.setText("");
        antwort.setVisible(false);
        frage.setWrapText(true);
        antwort.setWrapText(true);

        lernkartenList = new ArrayList<>();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/lernkarten/getlernkarten/" + lernkartensetId)).build();
        HttpResponse<String> response;


        try {

            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            lernkartenList = mapper.readValue(response.body(), new TypeReference<List<Lernkarte>>() {});

        } catch (Exception e) {
            e.printStackTrace();
        }

        if(lernkartenList != null && lernkartenList.size() >= 1) {

            currentLernkarte = lernkartenList.get(0);
            frage.setText("Frage: " + currentLernkarte.getFrage());
            lernkartenanzahl.setText("Anzahl Lernkarten: " + lernkartenList.size());
        } else {
            frage.setText("Es existieren keine Lernkarten");
            antwort.setText("");
            lernkartenanzahl.setText("Anzahl Lernkarten: 0");
        }
    }

    public void ActionLösung() {
        if(currentLernkarte == null)
            return;
        showLösung = !showLösung;

        if(showLösung) {
            antwort.setText("Antwort: " + currentLernkarte.getAntwort());
            antwort.setVisible(true);

        } else {
            antwort.setText("");
            antwort.setVisible(false);
        }
    }

    public void ActionPrevious() {
        if(currentLernkarte == null)
            return;

        if(currentPositionInLernkartenList - 1 < 0)
            return;

        currentLernkarte = lernkartenList.get(currentPositionInLernkartenList - 1);
        currentPositionInLernkartenList--;
        initLernKarte(currentLernkarte);
    }

    public void ActionNext() {
        if(currentLernkarte == null)
            return;

        if(currentPositionInLernkartenList + 1 >= lernkartenList.size())
            return;

        currentLernkarte = lernkartenList.get(currentPositionInLernkartenList + 1);
        currentPositionInLernkartenList++;
        initLernKarte(currentLernkarte);
    }

    public void ActionKartenmischen() {
        if(currentLernkarte == null)
            return;

        Collections.shuffle(lernkartenList);
        currentPositionInLernkartenList = 0;
        currentLernkarte = lernkartenList.get(0);
        initLernKarte(currentLernkarte);
    }

    public void ActionCreate() {
        layout.instanceLayout("lernkarteErstellen.fxml");
        ((LernKartenErstellenController) layout.getController()).setLayout(layout);
        ((LernKartenErstellenController) layout.getController()).setProjektgruppe(projektgruppe);
        ((LernKartenErstellenController) layout.getController()).setNutzer(nutzer);
        ((LernKartenErstellenController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
        ((LernKartenErstellenController) layout.getController()).setLernkartenset(lernkartenset);
        ((LernKartenErstellenController) layout.getController()).initLernKartenErstellen();
    }

    public void ActionBack() {
        layout.instanceLayout("lernkartensets.fxml");
        ((LernkartenController) layout.getController()).setLayout(layout);
        ((LernkartenController) layout.getController()).setNutzer(nutzer);
        ((LernkartenController) layout.getController()).setProjektgruppe(projektgruppe);
        ((LernkartenController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
        ((LernkartenController) layout.getController()).populateLernkartensets();
        ((LernkartenController) layout.getController()).lernkartensetsPgTitel.setText(projektgruppe.getTitel());
        ((LernkartenController) layout.getController()).lernkartensetsLvTitel.setText(lehrveranstaltung.getTitel());
    }

    public void initLernKarte(Lernkarte lernkarte) {
        frage.setText("Frage: " + lernkarte.getFrage());
        showLösung = false;
        antwort.setText("");
        antwort.setVisible(false);
    }

    public void populateLernkartensets() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = null;
        if(nutzer instanceof Student) {
            request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/lernkarten/eigeneLernkartensets/" + projektgruppe.getId() + "&" + ((Student) nutzer).getId())).build();
        }
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            List<Lernkartenset> eigeneSets = mapper.readValue(response.body(), new TypeReference<List<Lernkartenset>>() {});

            lernkartensetColumn.setCellValueFactory(new PropertyValueFactory<Lernkartenset,String>("bezeichnung"));
            eigeneSetsId.setCellValueFactory(new PropertyValueFactory<Lernkartenset,Integer>("id"));

            lernkartensetColumn.setCellFactory(tablecell -> {
                TableCell<Lernkartenset, String> cell = new TableCell<Lernkartenset, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty) ;
                        setText(empty ? null : item);
                    }
                };
                cell.setCursor(Cursor.HAND);
                cell.setOnMouseClicked(e -> {
                            if (!cell.isEmpty()) {
                                lernkartenset = cell.getTableRow().getItem();
                                redirectToLernkartenset(cell.getTableRow().getItem().getId());
                            }
                        }
                );
                return cell;
            });
            ObservableList<Lernkartenset> obsEigeneSets = FXCollections.observableList(eigeneSets);
            eigeneLernkartensets.setItems(obsEigeneSets);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/lernkarten/geteilteLernkartensets/" + projektgruppe.getId())).build();
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            List<Lernkartenset> geteilteSets = mapper.readValue(response.body(), new TypeReference<List<Lernkartenset>>() {});

            geteilteLernkartensetsColumn.setCellValueFactory(new PropertyValueFactory<Lernkartenset,String>("bezeichnung"));
            geteilteSetsId.setCellValueFactory(new PropertyValueFactory<Lernkartenset,Integer>("id"));
            erstellerColumn.setCellValueFactory(new PropertyValueFactory<Lernkartenset,String>("erstellerVorname"));

            geteilteLernkartensetsColumn.setCellFactory(tablecell -> {
                TableCell<Lernkartenset, String> cell = new TableCell<Lernkartenset, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty) ;
                        setText(empty ? null : item);
                    }
                };
                cell.setCursor(Cursor.HAND);
                cell.setOnMouseClicked(e -> {
                            if (!cell.isEmpty()) {
                                redirectToLernkartenset(cell.getTableRow().getItem().getId());
                            }
                        }
                );
                return cell;
            });
            erstellerColumn.setCellFactory(tablecell -> {
                TableCell<Lernkartenset, String> cell = new TableCell<Lernkartenset, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty) ;
                        setText(empty ? null : item);
                    }
                };
                cell.setCursor(Cursor.HAND);
                cell.setOnMouseClicked(e -> {
                            if (!cell.isEmpty()) {
                                redirectToLernkartenset(cell.getTableRow().getItem().getId());
                            }
                        }
                );
                return cell;
            });

            ObservableList<Lernkartenset> obsGeteilteSets = FXCollections.observableList(geteilteSets);
            geteilteLernkartensets.setItems(obsGeteilteSets);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void redirectToLernkartenset(long id) {
        layout.instanceLayout("lernkarteAnsicht.fxml");
        ((LernkartenController) layout.getController()).setLayout(layout);
        ((LernkartenController) layout.getController()).setLernkartenset(lernkartenset);
        ((LernkartenController) layout.getController()).setProjektgruppe(projektgruppe);
        ((LernkartenController) layout.getController()).setNutzer(nutzer);
        ((LernkartenController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
        ((LernkartenController) layout.getController()).initLernkartenController(id);
    }

    public void neuenLernkartensetErstellenWeiterleitung(ActionEvent actionEvent) {
        layout.instanceLayout("lernsetErstellen.fxml");
        ((LernkartenController) layout.getController()).setLayout(layout);
        ((LernkartenController) layout.getController()).setNutzer(nutzer);
        ((LernkartenController) layout.getController()).setProjektgruppe(projektgruppe);
        ((LernkartenController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
        ((LernkartenController) layout.getController()).setErstellenPgTitel.setText(projektgruppe.getTitel());
        ((LernkartenController) layout.getController()).setErstellenLvTitel.setText(lehrveranstaltung.getTitel());
    }

    public void lernkartensetTeilenWeiterleitung(ActionEvent actionEvent) {
        layout.instanceLayout("lernkartensetsTeilen.fxml");
        ((LernkartenController) layout.getController()).setLayout(layout);
        ((LernkartenController) layout.getController()).setNutzer(nutzer);
        ((LernkartenController) layout.getController()).setProjektgruppe(projektgruppe);
        ((LernkartenController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
        ((LernkartenController) layout.getController()).pgTitel.setText(projektgruppe.getTitel());
        ((LernkartenController) layout.getController()).lvTitel.setText(lehrveranstaltung.getTitel());
        ((LernkartenController) layout.getController()).populateGeteilteUngeteilteLernkartensets();
        ((LernkartenController) layout.getController()).addCheckBoxToTable("ungeteilte");

    }

    public void populateGeteilteUngeteilteLernkartensets() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/lernkarten/geteilteLernkartensets/" + projektgruppe.getId())).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            List<Lernkartenset> geteilteSetsListe = mapper.readValue(response.body(), new TypeReference<List<Lernkartenset>>() {});

            geteilteSetsColumn.setCellValueFactory(new PropertyValueFactory<Lernkartenset, String>("bezeichnung"));

            ObservableList<Lernkartenset> obsGeteilteSets = FXCollections.observableList(geteilteSetsListe);
            geteilteSets.setItems(obsGeteilteSets);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/lernkarten/ungeteilteLernkartensets/" + projektgruppe.getId() + "&" + ((Student) nutzer).getId())).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            List<Lernkartenset> ungeteilteSetsListe = mapper.readValue(response.body(), new TypeReference<List<Lernkartenset>>() {});

            ungeteilteSets.setEditable(true);
            ungeteilteSetsColumn.setCellValueFactory(new PropertyValueFactory<Lernkartenset, String>("bezeichnung"));

            ObservableList<Lernkartenset> obsUngeteilteSets = FXCollections.observableList(ungeteilteSetsListe);
            ungeteilteSets.setItems(obsUngeteilteSets);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void addCheckBoxToTable(String table) {
        TableColumn<Lernkartenset, Void> colBtn = new TableColumn("Selected");
        Callback<TableColumn<Lernkartenset, Void>, TableCell<Lernkartenset, Void>> cellFactory = new Callback<TableColumn<Lernkartenset, Void>, TableCell<Lernkartenset, Void>>() {
            @Override
            public TableCell<Lernkartenset, Void> call(final TableColumn<Lernkartenset, Void> param) {
                final TableCell<Lernkartenset, Void> cell = new TableCell<Lernkartenset, Void>() {
                    private final CheckBox checkBox = new CheckBox();
                    {
                        checkBox.setOnAction((ActionEvent event) -> {
                            Lernkartenset lernkartenset = getTableView().getItems().get(getIndex());
                            if (checkBox.isSelected()) {        // wenn Häckchen gesetzt wird, füge Student zu der Liste der ausgewählten Studenten hinzu
                                selectedSetsIds.add((long)lernkartenset.getId());
                            } else {                            // wenn Häckchen entfernt wird, entferne den Studenten von der Liste
                                for (int i = 0; i < selectedSetsIds.size(); i++) {
                                    if (selectedSetsIds.get(i) == lernkartenset.getId()) {
                                        selectedSetsIds.remove(i);
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
        if (table.equals("ungeteilte")) {
            ungeteilteSets.getColumns().add(colBtn);
        }
    }

    public void lernkartensetsTeilen(ActionEvent actionEvent) {
        actionEvent.consume();
        try(CloseableHttpClient client = HttpClients.createDefault()) {
            String url = "http://localhost:8080/lernkarten/teileLernkartensets";
            HttpPost post = new HttpPost(url);
            MultipartEntityBuilder entity = MultipartEntityBuilder.create();
            entity.setCharset(StandardCharsets.UTF_8);
            entity.addTextBody("projektgruppenId", String.valueOf(projektgruppe.getId()), ContentType.create("text/plain", MIME.UTF8_CHARSET));
            if (selectedSetsIds.size() > 0) {
                for (int i = 0; i < selectedSetsIds.size(); i++) {
                    entity.addPart("lernkartensetId", new StringBody(selectedSetsIds.get(i).toString(), ContentType.create("text/plain", MIME.UTF8_CHARSET)));
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Keine Lernkartensets ausgewählt!");
                alert.setHeaderText("Es wurden keine Lernkartensets zum Teilen in dieser Projektgruppe ausgewählt!");
                alert.setContentText("Wählen Sie bitte Lernkartensets aus, die geteilt werden sollen.");
                alert.showAndWait();
                return;
            }
            HttpEntity requestEntity = entity.build();
            post.setEntity(requestEntity);

            try (CloseableHttpResponse response = client.execute(post)) {
                selectedSetsIds.clear();
                populateGeteilteUngeteilteLernkartensets();
                ungeteilteSets.getColumns().remove(2);
                addCheckBoxToTable("ungeteilte");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void teilenZurueckPressedButton(ActionEvent actionEvent) {
        actionEvent.consume();
        layout.instanceLayout("lernkartensets.fxml");
        ((LernkartenController) layout.getController()).setLayout(layout);
        ((LernkartenController) layout.getController()).setNutzer(nutzer);
        ((LernkartenController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
        ((LernkartenController) layout.getController()).setProjektgruppe(projektgruppe);
        ((LernkartenController) layout.getController()).populateLernkartensets();
        ((LernkartenController) layout.getController()).lernkartensetsPgTitel.setText(projektgruppe.getTitel());
        ((LernkartenController) layout.getController()).lernkartensetsLvTitel.setText(lehrveranstaltung.getTitel());
    }

    public void setErstellenAbbrechen(ActionEvent actionEvent) {
        actionEvent.consume();
        layout.instanceLayout("lernkartensets.fxml");
        ((LernkartenController) layout.getController()).setLayout(layout);
        ((LernkartenController) layout.getController()).setNutzer(nutzer);
        ((LernkartenController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
        ((LernkartenController) layout.getController()).setProjektgruppe(projektgruppe);
        ((LernkartenController) layout.getController()).populateLernkartensets();
        ((LernkartenController) layout.getController()).lernkartensetsPgTitel.setText(projektgruppe.getTitel());
        ((LernkartenController) layout.getController()).lernkartensetsLvTitel.setText(lehrveranstaltung.getTitel());
    }

    public void lernkartensetErstellen(ActionEvent actionEvent) {
        actionEvent.consume();
        String bezeichnung = bezTextfield.getText().trim();
        if (bezeichnung.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Keine Bezeichnung vorhanden!");
            alert.setHeaderText("Es wurde keine Bezeichnung für den neuen Lernkartenset eingegeben!");
            alert.setContentText("Geben Sie bitte eine Bezeichnung ein.");
            alert.showAndWait();
            return;
        }
        int nutzerId = ((Student) nutzer).getId();

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost("http://localhost:8080/lernkarten/createLernkartenset");
        MultipartEntityBuilder entity = MultipartEntityBuilder.create();
        entity.setCharset(StandardCharsets.UTF_8);
        entity.addTextBody("bezeichnung", bezeichnung, ContentType.create("text/plain", MIME.UTF8_CHARSET));
        entity.addTextBody("erstellerId", String.valueOf(nutzerId), ContentType.create("text/plain", MIME.UTF8_CHARSET));
        entity.addTextBody("projektgruppenId", String.valueOf(projektgruppe.getId()), ContentType.create("text/plain", MIME.UTF8_CHARSET));

        HttpEntity requestEntity = entity.build();
        post.setEntity(requestEntity);

        try (CloseableHttpResponse response = client.execute(post)){
            HttpEntity responseEntity = response.getEntity();
            String responseBody = EntityUtils.toString(responseEntity);

            if(responseBody.contentEquals("Lernkartenset erfolgreich erstellt")) {     // Lernkartenset wurde erstellt
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Lernkartenset erfolgreich erstellt!");
                alert.setHeaderText("Ihr Lernkartenset wurde erfolgreich erstellt");
                alert.setContentText("Zurück zu den Lernkartensets");
                alert.showAndWait();
                layout.instanceLayout("lernkartensets.fxml");
                ((LernkartenController) layout.getController()).setLayout(layout);
                ((LernkartenController) layout.getController()).setNutzer(nutzer);
                ((LernkartenController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
                ((LernkartenController) layout.getController()).setProjektgruppe(projektgruppe);
                ((LernkartenController) layout.getController()).populateLernkartensets();
                ((LernkartenController) layout.getController()).lernkartensetsLvTitel.setText(lehrveranstaltung.getTitel());
                ((LernkartenController) layout.getController()).lernkartensetsPgTitel.setText(projektgruppe.getTitel());

            } else if (responseBody.contentEquals("Lernkartenset mit dieser Bezeichnung bereits vorhanden")){        // Lernkartenset existiert bereits mit diesem Titel
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erstellung nicht erfolgreich!");
                alert.setHeaderText("Es ist bereits ein Lernkartenset mit dieser Bezeichnung vorhanden.");
                alert.setContentText("Geben Sie eine andere Bezeichnung ein");
                alert.showAndWait();
            } else {        // unbekannter Fehler
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erstellung nicht erfolgreich!");
                alert.setHeaderText("Es ist ein unbekannter Fehler aufgetreten.");
                alert.showAndWait();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
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
//        lernkartensetsLvTitel.setText("Lehrveranstaltung " + lehrveranstaltung.getTitel());
    }

    public void setProjektgruppe(Projektgruppe projektgruppe) {
        this.projektgruppe = projektgruppe;
//        lernkartensetsPgTitel.setText("Projektgruppe " + projektgruppe.getTitel());
    }

    public Lernkartenset getLernkartenset() {
        return lernkartenset;
    }

    public void setLernkartenset(Lernkartenset lernkartenset) {
        this.lernkartenset = lernkartenset;
    }
}


