package Client.Controller;

import Client.Layouts.Layout;
import Client.Modell.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
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

import javax.swing.*;
import java.beans.EventHandler;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ProjektgruppenController {
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

    private Object nutzer;
    private Lehrveranstaltung lehrveranstaltung;
    private Projektgruppe projektgruppe;
    private List<Long> selectedStudentIds;


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
        uebersichtPGTitel_label.setText(projektgruppe.getTitel());
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
                    Layout projektgruppeoverview = new Layout ("projektgruppeUebersicht.fxml", (Stage) pgListe_tableview.getScene().getWindow(), nutzer);
                    if (projektgruppeoverview.getController() instanceof ProjektgruppenController) {
                        ((ProjektgruppenController) projektgruppeoverview.getController()).setNutzer(nutzer);
                        ((ProjektgruppenController) projektgruppeoverview.getController()).setProjektgruppe(projektgruppe);
                        ((ProjektgruppenController) projektgruppeoverview.getController()).setLehrveranstaltung(lehrveranstaltung);
                        ((ProjektgruppenController) projektgruppeoverview.getController()).setPGUebersichtLvTitel(lehrveranstaltung.getTitel());
                        ((ProjektgruppenController) projektgruppeoverview.getController()).setPGUebersichtPGTitel(projektgruppe.getTitel());
                    }
                } else {        //Student ist noch nicht Mitglied -> Beitrittsseite
                    Layout projektgruppebeitritt = new Layout("projektgruppenbeitritt.fxml", (Stage) pgListe_tableview.getScene().getWindow(), nutzer);
                    if (projektgruppebeitritt.getController() instanceof ProjektgruppeBeitretenController) {
                        ((ProjektgruppeBeitretenController) projektgruppebeitritt.getController()).setNutzer(nutzer);
                        ((ProjektgruppeBeitretenController) projektgruppebeitritt.getController()).setProjektgruppe(projektgruppe);
                        ((ProjektgruppeBeitretenController) projektgruppebeitritt.getController()).setLehrveranstaltung(lehrveranstaltung);
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

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
                ((ProjektgruppenController) neuePGerstellen.getController()).populateTeilnehmerTableView();
                ((ProjektgruppenController) neuePGerstellen.getController()).selectedStudentIds = new ArrayList<>();
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

    public void populateTeilnehmerTableView() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/projektgruppe/studteilnehmer/" + lehrveranstaltung.getId())).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            List<Student> students = mapper.readValue(response.body(), new TypeReference<List<Student>>() {});

            addCheckBoxToTable();
            checkbox_col.setVisible(false);
            studentenliste_tableview.setEditable(true);
            studentenname_col.setCellValueFactory(new PropertyValueFactory<Student, String>("NachnameVorname"));
            matrnr_col.setCellValueFactory(new PropertyValueFactory<Student, Integer>("matrikelnummer"));

            ObservableList<Student> obsStud = FXCollections.observableList(students);
            studentenliste_tableview.setItems(obsStud);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void addCheckBoxToTable() {
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
                            System.out.println(selectedStudentIds);
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
        studentenliste_tableview.getColumns().add(colBtn);

    }

    // anklicken von "Projektgruppe erstellen"-Button auf der Seite zur Erstellung einer neuen Projektgruppe
    public void erstellenPressedButton(ActionEvent actionEvent) {
        System.out.println(selectedStudentIds);
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
                System.out.println("ist im else-zweig angekommen");
                entity.addPart("studentId", new StringBody(String.valueOf(-1), ContentType.create("text/plain", MIME.UTF8_CHARSET)));
            }
            HttpEntity requestEntity = entity.build();
            post.setEntity(requestEntity);

            try (CloseableHttpResponse response = client.execute(post)) {
                HttpEntity responseEntity = response.getEntity();
                String responseBody = EntityUtils.toString(responseEntity);

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

    public void chatPressedButton(ActionEvent actionEvent) {
    }

    public void todoPressedButton(ActionEvent actionEvent) {
    }

    public void filesPressedButton(ActionEvent actionEvent) {
    }

    public void memberPressedButton(ActionEvent actionEvent) {
    }


}
