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

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LernkartenController {

    @FXML private Label lernkartensetsPgTitel;
    @FXML private Label lernkartensetsLvTitel;
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


    private List<Lernkarte> lernkartenList;

    private Lernkarte currentLernkarte;

    int currentPositionInLernkartenList;

    boolean showLösung;


    public void initLernkartenController() {

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
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/lernkarten/getlernkarten/" + projektgruppe.getId())).build();
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
        layout.instanceLayout("LernkarteErstellen.fxml");
        ((LernKartenErstellenController) layout.getController()).setLayout(layout);
        ((LernKartenErstellenController) layout.getController()).setProjektgruppe(projektgruppe);
        ((LernKartenErstellenController) layout.getController()).setNutzer(nutzer);
        ((LernKartenErstellenController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
        ((LernKartenErstellenController) layout.getController()).initLernKartenErstellen();
    }

    public void ActionBack() {
        layout.instanceLayout("projektgruppeUebersicht.fxml");
        ((ProjektgruppenController) layout.getController()).setLayout(layout);
        ((ProjektgruppenController) layout.getController()).setNutzer(nutzer);
        ((ProjektgruppenController) layout.getController()).setProjektgruppe(projektgruppe);
        ((ProjektgruppenController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
        ((ProjektgruppenController) layout.getController()).setPGUebersichtLvTitel(lehrveranstaltung.getTitel());
        ((ProjektgruppenController) layout.getController()).setPGUebersichtPGTitel(projektgruppe.getTitel());
        ((ProjektgruppenController) layout.getController()).setChautraumId((int) projektgruppe.getChat().getId());
        ((ProjektgruppenController) layout.getController()).populateMaterialTable();
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
    }

    public void neuenLernkartensetErstellen(ActionEvent actionEvent) {
        layout.instanceLayout("lernsetErstellen.fxml");
        ((LernkartenController) layout.getController()).setLayout(layout);
        ((LernkartenController) layout.getController()).setErstellenPgTitel.setText(projektgruppe.getTitel());
        ((LernkartenController) layout.getController()).setErstellenLvTitel.setText(lehrveranstaltung.getTitel());
    }

    public void lernkartensetTeilenWeiterleitung(ActionEvent actionEvent) {
        layout.instanceLayout("lernkartensetsTeilen.fxml");
        ((LernkartenController) layout.getController()).setLayout(layout);
        ((LernkartenController) layout.getController()).pgTitel.setText(projektgruppe.getTitel());
        ((LernkartenController) layout.getController()).lvTitel.setText(lehrveranstaltung.getTitel());
    }

    public void lernkartensetsTeilen(ActionEvent actionEvent) {
    }

    public void teilenZurueckPressedButton(ActionEvent actionEvent) {
    }

    public void setErstellenAbbrechen(ActionEvent actionEvent) {
    }

    public void lernkartensetErstellen(ActionEvent actionEvent) {
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
        lernkartensetsLvTitel.setText("Lehrveranstaltung " + lehrveranstaltung.getTitel());
    }

    public void setProjektgruppe(Projektgruppe projektgruppe) {
        this.projektgruppe = projektgruppe;
        lernkartensetsPgTitel.setText("Projektgruppe " + projektgruppe.getTitel());
    }


}


