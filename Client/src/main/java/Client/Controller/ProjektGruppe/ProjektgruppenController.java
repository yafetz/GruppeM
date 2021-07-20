package Client.Controller.ProjektGruppe;

import Client.Controller.Chat.ChatController;
import Client.Controller.Gruppenmitglieder.GruppenUploadController;
import Client.Controller.Gruppenmitglieder.GruppenmitgliederController;
import Client.Controller.Lernkarten.LernkartenController;
import Client.Controller.ToDo.ToDoListeController;
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
import javafx.util.Callback;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;

public class ProjektgruppenController {

    @FXML private Button mitgliederZurueckButton;
    @FXML private Label MitgliederPGTitel_label;
    @FXML private Label MitgliederLvTitel_label;
    @FXML private TableView<Student> vorhandene_TableView;
    @FXML private TableColumn<Student, String> vorhandeneName_Col;
    @FXML private TableColumn<Student, Integer> vorhandeneMatr_Col;
    @FXML private Button addMitgliederButton;
    @FXML private TableView<Student> neue_TableView;
    @FXML private TableColumn<Student, String> neueName_Col;
    @FXML private TableColumn<Student, Integer> neueMatr_Col;
    @FXML private Button suchenButton;
    @FXML private Label addStud_label;
    @FXML private ScrollPane scrollpane;
    @FXML private Label uebersichtPGTitel_label;
    @FXML private Label uebersichtLvTitel_label;
    @FXML private Button chatButton;
    @FXML private Button todoButton;
    @FXML private Button filesButton;
    @FXML private Button memberButton;
    @FXML private Label pglisteseitentitel_label;
    @FXML private Button pgErstellen_btn;
    @FXML private TextField suchen_txtfield;
    @FXML private TableView<Projektgruppe> pgListe_tableview;
    @FXML private TableColumn<Projektgruppe, String> pgTitel_col;
    @FXML private TableColumn<Projektgruppe, Integer> nrMitglieder_col;
    @FXML private TableColumn<Projektgruppe, Long> pgId_col;
    @FXML private Label erstellenLvTitel_label;
    @FXML private TextField pgTitel_txtfield;
    @FXML private TableView<Student> studentenliste_tableview;
    @FXML private TableColumn<Student, Boolean> checkbox_col;
    @FXML private TableColumn<Student, String> studentenname_col;
    @FXML private TableColumn<Student, Integer> matrnr_col;
    @FXML private Button erstellen_btn;
    @FXML private Button lernkarten;

    @FXML
    private TableView<Gruppenmaterial> MaterialListe;
    @FXML
    private TableColumn<Gruppenmaterial, String> gruppenmaterial;

    public long getGruppenid() {
        return gruppenid;
    }

    public void setGruppenid(long gruppenid) {
        this.gruppenid = gruppenid;
    }

    long gruppenid;

    private Object nutzer;
    private Lehrveranstaltung lehrveranstaltung;
    private Projektgruppe projektgruppe;
    private List<Long> selectedStudentIds = new ArrayList<>();

    private int chautraumId;

    public int getChautraumId() {
        return chautraumId;
    }

    public void setChautraumId(int chautraumId) {
        this.chautraumId = chautraumId;
    }

    private Layout layout;

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
        setNutzer(nutzer);
    }

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
    }

    public void setPGListeSeitentitel(String titelLehrveranstaltung) {
        pglisteseitentitel_label.setText("Projektgruppen der Lehrveranstaltung " + lehrveranstaltung.getTitel());
    }

    public void setPGUebersichtLvTitel(String titelLehrveranstaltung) {
        uebersichtLvTitel_label.setText("der Lehrveranstaltung " + titelLehrveranstaltung);
    }

    public void setPGUebersichtPGTitel(String titelProjektgruppe) {
        uebersichtPGTitel_label.setText(titelProjektgruppe);
    }

    public Projektgruppe getProjektgruppe() {
        return projektgruppe;
    }

    public void setProjektgruppe(Projektgruppe projektgruppe) {
        this.projektgruppe = projektgruppe;
        //uebersichtPGTitel_label.setText(projektgruppe.getTitel());
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
                                redirectToProjektgruppe(cell.getTableRow().getItem().getId());
                            }
                        }
                );
                return cell;
            });

            if (nutzer instanceof Student) {
                for (Projektgruppe pg : projektgruppen) {
                    request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/projektgruppe/checkMember/" + pg.getId() + "&" + ((Student) nutzer).getId())).build();
                    try {
                        HttpResponse<String> istGruppenmitgliedResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
                        if (istGruppenmitgliedResponse.body().equals("true")) {
                            pg.setTitel(pg.getTitel() + " (beigetreten)");
                        }
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            ObservableList<Projektgruppe> obsPG = FXCollections.observableList(projektgruppen);
            pgListe_tableview.setItems(obsPG);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void redirectToProjektgruppe(Long id) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/projektgruppe/" + id)).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            Projektgruppe projektgruppe = mapper.readValue(response.body(), Projektgruppe.class);
            if (nutzer instanceof Student) {        //nur Studenten dürfen Projektgruppen beitreten
                long studentID = ((Student) nutzer).getId();
                request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/projektgruppe/checkMember/" + id + "&" + studentID)).build();
                HttpResponse<String> istGruppenmitgliedResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (istGruppenmitgliedResponse.body().equals("true")) {         //der Student ist bereits Mitglied dieser Projektgruppe -> Übersichtsseite
                    layout.instanceLayout("projektgruppeUebersicht.fxml");
                    ((ProjektgruppenController) layout.getController()).setLayout(layout);
                    ((ProjektgruppenController) layout.getController()).setNutzer(nutzer);


                    if (layout.getController() instanceof ProjektgruppenController) {
                        ((ProjektgruppenController) layout.getController()).setProjektgruppe(projektgruppe);
                        ((ProjektgruppenController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
                        ((ProjektgruppenController) layout.getController()).setPGUebersichtLvTitel(lehrveranstaltung.getTitel());
                        ((ProjektgruppenController) layout.getController()).setPGUebersichtPGTitel(projektgruppe.getTitel());
                        ((ProjektgruppenController) layout.getController()).setChautraumId((int) projektgruppe.getChat().getId());
                        ((ProjektgruppenController) layout.getController()).populateMaterialTable();
                    }
                } else {        //Student ist noch nicht Mitglied -> Beitrittsseite
                    layout.instanceLayout("projektgruppenbeitritt.fxml");
                    ((ProjektgruppeBeitretenController) layout.getController()).setLayout(layout);
                    if (layout.getController() instanceof ProjektgruppeBeitretenController) {
                        ((ProjektgruppeBeitretenController) layout.getController()).setNutzer(nutzer);
                        ((ProjektgruppeBeitretenController) layout.getController()).setProjektgruppe(projektgruppe);
                        ((ProjektgruppeBeitretenController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
                    }
                }
            }else if (nutzer instanceof Lehrender) {
                layout.instanceLayout("projektgruppeMitgliederansichtLehrender.fxml");
                if (layout.getController() instanceof ProjektgruppenController) {
                    ((ProjektgruppenController) layout.getController()).setLayout(layout);
                    ((ProjektgruppenController) layout.getController()).setNutzer(nutzer);
                    ((ProjektgruppenController) layout.getController()).setProjektgruppe(projektgruppe);
                    ((ProjektgruppenController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
                    ((ProjektgruppenController) layout.getController()).MitgliederPGTitel_label.setText(projektgruppe.getTitel());
                    ((ProjektgruppenController) layout.getController()).MitgliederLvTitel_label.setText("der Lehrveranstaltung " + lehrveranstaltung.getTitel());
                    ((ProjektgruppenController) layout.getController()).populateVorhandeneTableView();
                    ((ProjektgruppenController) layout.getController()).populateNeueTableView();
                    ((ProjektgruppenController) layout.getController()).addCheckBoxToTable("neue");
                    ((ProjektgruppenController) layout.getController()).selectedStudentIds = new ArrayList<>();
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // anklicken von "Neue Projektgruppe erstellen"-Button auf der Seite der Projektgruppenliste
    public void pgListeErstellenPressedButton(ActionEvent actionEvent) {
        actionEvent.consume();
        layout.instanceLayout("projektgruppeErstellen.fxml");
        ((ProjektgruppenController) layout.getController()).setLayout(layout);
        if (nutzer instanceof Lehrender) {
            if (layout.getController() instanceof ProjektgruppenController) {
                ((ProjektgruppenController) layout.getController()).setErstellenLvTitel_label(lehrveranstaltung.getTitel());
                ((ProjektgruppenController) layout.getController()).setNutzer(nutzer);
                ((ProjektgruppenController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
                ((ProjektgruppenController) layout.getController()).populateTeilnehmerTableView();
            }
        } else if (nutzer instanceof Student) {
            if (layout.getController() instanceof ProjektgruppenController) {
                ((ProjektgruppenController) layout.getController()).setErstellenLvTitel_label(lehrveranstaltung.getTitel());
                ((ProjektgruppenController) layout.getController()).addStud_label.setVisible(false);
                ((ProjektgruppenController) layout.getController()).studentenliste_tableview.setVisible(false);
                ((ProjektgruppenController) layout.getController()).scrollpane.setVisible(false);
                ((ProjektgruppenController) layout.getController()).setNutzer(nutzer);
                ((ProjektgruppenController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
            }
        }
    }

    public void populateTeilnehmerTableView() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/projektgruppe/studteilnehmer/" + lehrveranstaltung.getId())).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            List<Student> students = mapper.readValue(response.body(), new TypeReference<List<Student>>() {});

            addCheckBoxToTable("teilnehmer");
            checkbox_col.setVisible(false);
            studentenliste_tableview.setEditable(true);
            studentenname_col.setCellValueFactory(new PropertyValueFactory<Student, String>("NachnameVorname"));
            matrnr_col.setCellValueFactory(new PropertyValueFactory<Student, Integer>("matrikelnummer"));

            ObservableList<Student> obsStud = FXCollections.observableList(students);
            studentenliste_tableview.setItems(obsStud);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void populateMaterialTable() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/gruppenmaterial/" + projektgruppe.getId())).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            //JSONArray jsonObject = new JSONArray(response.body());

            ObjectMapper objectMapper = new ObjectMapper();
            List<Gruppenmaterial> gruppenmaterials = objectMapper.readValue(response.body(), new TypeReference<List<Gruppenmaterial>>(){});

            gruppenmaterial.setCellValueFactory(new PropertyValueFactory<>("Titel"));
            gruppenmaterial.setCellFactory(tablecell -> {
                TableCell<Gruppenmaterial, String> cell = new TableCell<Gruppenmaterial, String>(){
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty) ;
                        setText(empty ? null : item);
                    }
                };
                cell.setCursor(Cursor.HAND);
                cell.setOnMouseClicked(e -> {
                            if (!cell.isEmpty()) {
                                long id=cell.getTableRow().getItem().getId();
                                try {
                                    Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/sep","root","");
                                    PreparedStatement pstmt = connection.prepareStatement("select datei from gruppenmaterial WHERE id LIKE "+id);
                                    ResultSet rs = pstmt.executeQuery();
                                    String home = System.getProperty("user.home");
                                    File file = new File(home+"/Downloads/" + cell.getTableRow().getItem().getTitel().replace(" ","_").replace("?",""));
                                    FileOutputStream fo = new FileOutputStream(file);
                                    rs.next();
                                    Blob datei = rs.getBlob("datei");
                                    IOUtils.write(datei.getBinaryStream().readAllBytes(),fo);
                                    fo.close();
                                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                    alert.setTitle("Erfolgreich heruntergladen!");
                                    alert.setHeaderText("Ihre Datei wurden erfolgreich heruntergeladen!");
                                    alert.setContentText("Sie können Ihre Datei unter ihrem Downloads Ordner finden! Sie müssen das Programm schließen bevor Sie ihre Datei öffnen können. Sonst kann es passieren das ihr Betriebssystem ihnen Probleme macht!");
                                    alert.showAndWait();

                                } catch (IOException | SQLException exception) {
                                    exception.printStackTrace();
                                }
                            }
                        }
                );
                return cell;
            });
            ObservableList<Gruppenmaterial> obsLv = FXCollections.observableList(gruppenmaterials);
            MaterialListe.setItems(obsLv);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void addCheckBoxToTable(String table) {
        TableColumn<Student, Void> colBtn = new TableColumn("Selected");
        Callback<TableColumn<Student, Void>, TableCell<Student, Void>> cellFactory = new Callback<TableColumn<Student, Void>, TableCell<Student, Void>>() {
            @Override
            public TableCell<Student, Void> call(final TableColumn<Student, Void> param) {
                final TableCell<Student, Void> cell = new TableCell<Student, Void>() {
                    private final CheckBox checkBox = new CheckBox();
                    {
                        checkBox.setOnAction((ActionEvent event) -> {
                            Student student = getTableView().getItems().get(getIndex());
                            if (checkBox.isSelected()) {        // wenn Häckchen gesetzt wird, füge Student zu der Liste der ausgewählten Studenten hinzu
                                selectedStudentIds.add((long)student.getId());
                            } else {                            // wenn Häckchen entfernt wird, entferne den Studenten von der Liste
                                for (int i = 0; i < selectedStudentIds.size(); i++) {
                                    if (selectedStudentIds.get(i) == student.getId()) {
                                        selectedStudentIds.remove(i);
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
        if (table.equals("teilnehmer")) {
            studentenliste_tableview.getColumns().add(colBtn);
        }
        if (table.equals("neue")) {
            neue_TableView.getColumns().add(colBtn);
        }
    }

    // anklicken von "Projektgruppe erstellen"-Button auf der Seite zur Erstellung einer neuen Projektgruppe
    public void erstellenPressedButton(ActionEvent actionEvent) {
        actionEvent.consume();
        String pgTitel = pgTitel_txtfield.getText().trim();
        if (pgTitel.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Kein Titel vorhanden!");
            alert.setHeaderText("Es wurde kein Titel für die neue Projektgruppe eingegeben!");
            alert.setContentText("Geben Sie bitte einen Titel ein.");
            alert.showAndWait();
            return;
        }
        long nutzerID = -1;
        if (nutzer instanceof Lehrender) {
            nutzerID = ((Lehrender) nutzer).getNutzerId().getId();
        } else if (nutzer instanceof Student) {
            nutzerID = ((Student) nutzer).getNutzer().getId();
        }

        try(CloseableHttpClient client = HttpClients.createDefault()) {
            String url = "http://localhost:8080/projektgruppe/neu";
            HttpPost post = new HttpPost(url);
            MultipartEntityBuilder entity = MultipartEntityBuilder.create();
            entity.setCharset(StandardCharsets.UTF_8);
            entity.addTextBody("titel", pgTitel, ContentType.create("text/plain", MIME.UTF8_CHARSET));
            entity.addTextBody("nutzer", String.valueOf(nutzerID), ContentType.create("text/plain", MIME.UTF8_CHARSET));
            entity.addTextBody("lehrveranstaltung", String.valueOf(lehrveranstaltung.getId()), ContentType.create("text/plain", MIME.UTF8_CHARSET));
            if (selectedStudentIds.size() > 0) {
                for (int i = 0; i < selectedStudentIds.size(); i++) {
                    entity.addPart("studentId", new StringBody(selectedStudentIds.get(i).toString(), ContentType.create("text/plain", MIME.UTF8_CHARSET)));
                }
            } else {
                entity.addPart("studentId", new StringBody(String.valueOf(-1), ContentType.create("text/plain", MIME.UTF8_CHARSET)));
            }
            HttpEntity requestEntity = entity.build();
            post.setEntity(requestEntity);

            try (CloseableHttpResponse response = client.execute(post)) {
                HttpEntity responseEntity = response.getEntity();
                String responseBody = EntityUtils.toString(responseEntity);

                if(responseBody.contentEquals("true")) {     // Projektgruppe wurde erstellt
                    selectedStudentIds.clear();
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Projektgruppe erfolgreich erstellt!");
                    alert.setHeaderText("Ihre Projektgruppe wurde erfolgreich erstellt");
                    alert.setContentText("Zurück zur Projektgruppenliste");
                    alert.showAndWait();
                   // Layout projektgruppenliste = new Layout("projektgruppenliste.fxml", (Stage) erstellen_btn.getScene().getWindow(), nutzer);
                    layout.instanceLayout("projektgruppenliste.fxml");
                    ((ProjektgruppenController) layout.getController()).setLayout(layout);
                    if (layout.getController() instanceof ProjektgruppenController) {
                        ((ProjektgruppenController) layout.getController()).setNutzer(nutzer);
                        ((ProjektgruppenController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
                        ((ProjektgruppenController) layout.getController()).setPageTitel("Projektgruppen der Lehrveranstaltung " + lehrveranstaltung.getTitel());
                        ((ProjektgruppenController) layout.getController()).populateTableView();
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

    public void suchenPressedButton(ActionEvent actionEvent) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            String url = "http://localhost:8080/projektgruppe/suchen";
            HttpPost post = new HttpPost(url);
            MultipartEntityBuilder entity = MultipartEntityBuilder.create();
            entity.setCharset(StandardCharsets.UTF_8);
            entity.addTextBody("titel", suchen_txtfield.getText().trim());
            entity.addTextBody("lvID", String.valueOf(lehrveranstaltung.getId()));
            HttpEntity requestEntity = entity.build();
            post.setEntity(requestEntity);
            try (CloseableHttpResponse response = client.execute(post)) {
                HttpEntity responseEntity = response.getEntity();
                String result = EntityUtils.toString(responseEntity);
                pgListe_tableview.getItems().removeAll();
                ObjectMapper mapper = new ObjectMapper();
                List<Projektgruppe> projektgruppen = mapper.readValue(result, new TypeReference<List<Projektgruppe>>() {});

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
                                    redirectToProjektgruppe(cell.getTableRow().getItem().getId());
                                }
                            }
                    );
                    return cell;
                });

                ObservableList<Projektgruppe> obsPG = FXCollections.observableList(projektgruppen);
                pgListe_tableview.setItems(obsPG);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void populateVorhandeneTableView() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/projektgruppe/" + projektgruppe.getId() + "/Mitglieder")).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            List<Student> students = mapper.readValue(response.body(), new TypeReference<List<Student>>() {});

            vorhandeneName_Col.setCellValueFactory(new PropertyValueFactory<Student, String>("NachnameVorname"));
            vorhandeneMatr_Col.setCellValueFactory(new PropertyValueFactory<Student, Integer>("matrikelnummer"));

            ObservableList<Student> obsStud = FXCollections.observableList(students);
            vorhandene_TableView.setItems(obsStud);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void populateNeueTableView() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/projektgruppe/" + projektgruppe.getId() + "/moeglicheMitglieder/" + lehrveranstaltung.getId())).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            List<Student> students = mapper.readValue(response.body(), new TypeReference<List<Student>>() {});

            neue_TableView.setEditable(true);
            neueName_Col.setCellValueFactory(new PropertyValueFactory<Student, String>("NachnameVorname"));
            neueMatr_Col.setCellValueFactory(new PropertyValueFactory<Student, Integer>("matrikelnummer"));

            ObservableList<Student> obsStud = FXCollections.observableList(students);
            neue_TableView.setItems(obsStud);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addMitgliederPressedButton(ActionEvent actionEvent) {
        actionEvent.consume();
        try(CloseableHttpClient client = HttpClients.createDefault()) {
            String url = "http://localhost:8080/projektgruppe/addMitglieder";
            HttpPost post = new HttpPost(url);
            MultipartEntityBuilder entity = MultipartEntityBuilder.create();
            entity.setCharset(StandardCharsets.UTF_8);
            entity.addTextBody("projektgruppenId", String.valueOf(projektgruppe.getId()), ContentType.create("text/plain", MIME.UTF8_CHARSET));
            if (selectedStudentIds.size() > 0) {
                for (int i = 0; i < selectedStudentIds.size(); i++) {
                    entity.addPart("studentId", new StringBody(selectedStudentIds.get(i).toString(), ContentType.create("text/plain", MIME.UTF8_CHARSET)));
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Keine Studenten ausgewählt!");
                alert.setHeaderText("Es wurden keine Studenten zum Hinzufügen zu dieser Projektgruppe ausgewählt!");
                alert.setContentText("Wählen Sie bitte Studenten aus, die hinzugefügt werden sollen.");
                alert.showAndWait();
                return;
            }
            HttpEntity requestEntity = entity.build();
            post.setEntity(requestEntity);

            try (CloseableHttpResponse response = client.execute(post)) {
                selectedStudentIds.clear();
                populateVorhandeneTableView();
                populateNeueTableView();
                neue_TableView.getColumns().remove(2);
                addCheckBoxToTable("neue");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void chatPressedButton(ActionEvent actionEvent) {
        layout.instanceLayout("chat.fxml");
        ((ChatController) layout.getController()).setLayout(layout);
        ((ChatController) layout.getController()).setChatraumid(chautraumId);
        ((ChatController) layout.getController()).setNutzer(nutzer);
        ((ChatController) layout.getController()).scheduler();

    }

    public void todoPressedButton(ActionEvent actionEvent) {
        layout.instanceLayout("toDoListe.fxml");
        ((ToDoListeController) layout.getController()).setLayout(layout);
        ((ToDoListeController) layout.getController()).setNutzerId(nutzer);
        ((ToDoListeController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
        ((ToDoListeController) layout.getController()).setProjektgruppe(projektgruppe);
        ((ToDoListeController) layout.getController()).populateTableView();
    }

    public void filesPressedButton(ActionEvent actionEvent) {
        actionEvent.consume();
        layout.instanceLayout("gruppenUpload.fxml");
        if (layout.getController() instanceof GruppenUploadController) {
            ((GruppenUploadController) layout.getController()).setLayout(layout);
            ((GruppenUploadController) layout.getController()).setNutzerInstanz(nutzer);
            ((GruppenUploadController) layout.getController()).setProjektgruppe(projektgruppe);
        }
    }

    public void memberPressedButton(ActionEvent actionEvent) throws IOException, InterruptedException {
        layout.instanceLayout("gruppenmitglieder.fxml");
        ((GruppenmitgliederController) layout.getController()).setLayout(layout);
        ((GruppenmitgliederController) layout.getController()).setNutzerId(nutzer);
        ((GruppenmitgliederController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
        ((GruppenmitgliederController) layout.getController()).setProjektgruppe(projektgruppe);
        ((GruppenmitgliederController) layout.getController()).populateTableView();

    }
    public void getMaterial(Projektgruppe projektgruppe) {
        this.projektgruppe=projektgruppe;
        long id = projektgruppe.getId();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/gruppenmaterial/" + id)).build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            JSONArray allegruppenmaterialien = new JSONArray(response.body());
            //List<Lehrmaterial> lehrmaterial = mapper.readValue(response.body(), new TypeReference<List<Lehrmaterial>>() {});
            gruppenmaterial.setCellValueFactory(new PropertyValueFactory<>("titel"));
            gruppenmaterial.setCellFactory(tablecell -> {
                TableCell<Gruppenmaterial, String> cell = new TableCell<Gruppenmaterial, String>(){
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty) ;
                        setText(empty ? null : item);
                    }
                };
                cell.setCursor(Cursor.HAND);
                cell.setOnMouseClicked(e -> {
                            if (!cell.isEmpty()) {
                                int lehrmaterialId = cell.getTableRow().getItem().getId().intValue();
                                try {
                                    Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/sep","root","");
                                    PreparedStatement pstmt = connection.prepareStatement("select datei from lehrmaterial WHERE id LIKE "+lehrmaterialId);
                                    ResultSet rs = pstmt.executeQuery();
                                    String home = System.getProperty("user.home");
                                    File file = new File(home+"/Downloads/" + allegruppenmaterialien.getJSONObject(cell.getTableRow().getIndex()).getString("titel").replace(" ","_").replace("?",""));
                                    FileOutputStream fo = new FileOutputStream(file);
                                    rs.next();
                                    Blob datei = rs.getBlob("datei");
                                    IOUtils.write(datei.getBinaryStream().readAllBytes(),fo);
                                    fo.close();
                                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                    alert.setTitle("Erfolgreich heruntergladen!");
                                    alert.setHeaderText("Ihre Lehrmaterialien wurden erfolgreich heruntergeladen!");
                                    alert.setContentText("Sie können Ihr Lernmaterial unter ihrem Downloads Ordner finden! Sie müssen das Programm schließen bevor Sie ihre Datei öffnen können. Sonst kann es passieren das ihr Betriebssystem ihnen Probleme macht!");
                                    alert.showAndWait();

                                } catch (IOException | SQLException exception) {
                                    exception.printStackTrace();
                                }
                            }
                        }
                );
                return cell;
            });

//            ObservableList is required to populate the table alleLv using .setItems() :
            for(int i = 0; i < allegruppenmaterialien.length(); i++){
                Gruppenmaterial l = new Gruppenmaterial();
                l.setId(allegruppenmaterialien.getJSONObject(i).getLong("id"));
                l.setTitel(allegruppenmaterialien.getJSONObject(i).getString("titel"));
                MaterialListe.getItems().add(l);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void zurueckPressedButton(ActionEvent actionEvent) {
        actionEvent.consume();
        layout.instanceLayout("projektgruppenliste.fxml");
        if (layout.getController() instanceof ProjektgruppenController) {
            ((ProjektgruppenController) layout.getController()).setLayout(layout);
            ((ProjektgruppenController) layout.getController()).setNutzer(nutzer);
            ((ProjektgruppenController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
            ((ProjektgruppenController) layout.getController()).populateTableView();
            ((ProjektgruppenController) layout.getController()).setPGListeSeitentitel(lehrveranstaltung.getTitel());
        }
    }

    public void lernkartenPressedButton(ActionEvent actionEvent) {
        actionEvent.consume();
        layout.instanceLayout("lernkartensets.fxml");
        ((LernkartenController) layout.getController()).setLayout(layout);
        ((LernkartenController) layout.getController()).setProjektgruppe(projektgruppe);
        ((LernkartenController) layout.getController()).setNutzer(nutzer);
        ((LernkartenController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
        ((LernkartenController) layout.getController()).populateLernkartensets();
        ((LernkartenController) layout.getController()).lernkartensetsPgTitel.setText(projektgruppe.getTitel());
        ((LernkartenController) layout.getController()).lernkartensetsLvTitel.setText(lehrveranstaltung.getTitel());
    }
}
