package Client.Controller;

import Client.Layouts.Layout;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import Client.Modell.*;
import javafx.event.ActionEvent;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.*;
public class LehrveranstaltungsuebersichtsseiteController {

    @FXML
    private Button projektgruppe_btn;
    @FXML
    private Button reviewErstellenBtn;
    @FXML
    private Button reviewStatistikBtn;
    @FXML
    private Button reviewBtn;
    @FXML
    private Label title;
    @FXML
    private Button materialUpload;
    @FXML
    private TableColumn<Lehrmaterial, String> teachMat;
    @FXML
    private TableView<Lehrmaterial> material;
    @FXML
    private Button teilnehmerListe;
    private Lehrveranstaltung lehrveranstaltung;
    private Object nutzer;
    @FXML
    private Button studentenliste;

    private Layout layout;

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public void Studenliste(ActionEvent actionEvent) {
        layout.instanceLayout("studentenListe.fxml");
        ((StudentenListeController) layout.getController()).setLayout(layout);
        ((StudentenListeController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
    }

    @FXML
    private void teilnehmerListe(ActionEvent event){
            layout.instanceLayout("teilnehmerListe.fxml");
            long veranstaltungId = ((Lehrveranstaltung) lehrveranstaltung).getId();
            ((TeilnehmerListeController) layout.getController()).setId(veranstaltungId);
            ((TeilnehmerListeController) layout.getController()).setLayout(layout);
            ((TeilnehmerListeController) layout.getController()).setLehrveranstaltung(((Lehrveranstaltung) lehrveranstaltung));
    }

    public void getMaterial(Lehrveranstaltung lehrkurs) {
        this.lehrveranstaltung=lehrkurs;
        long id = lehrkurs.getId();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/lehrmaterial/" + id)).build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            JSONArray alleLehrmaterialien = new JSONArray(response.body());
            //List<Lehrmaterial> lehrmaterial = mapper.readValue(response.body(), new TypeReference<List<Lehrmaterial>>() {});
            teachMat.setCellValueFactory(new PropertyValueFactory<>("titel"));
            teachMat.setCellFactory(tablecell -> {
                TableCell<Lehrmaterial, String> cell = new TableCell<Lehrmaterial, String>(){
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
                                    File file = new File(home+"/Downloads/" + alleLehrmaterialien.getJSONObject(cell.getTableRow().getIndex()).getString("titel").replace(" ","_").replace("?",""));
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
            for(int i = 0; i < alleLehrmaterialien.length(); i++){
                Lehrmaterial l = new Lehrmaterial();
                l.setId(alleLehrmaterialien.getJSONObject(i).getLong("id"));
                l.setTitel(alleLehrmaterialien.getJSONObject(i).getString("titel"));
                material.getItems().add(l);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
    }

    @FXML
    private void materialUploadPressedButton(ActionEvent event) {
        event.consume();
        layout.instanceLayout("lehrmaterialUpload.fxml");
        ((LehrmaterialController) layout.getController()).setLayout(layout);
        ((LehrmaterialController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
        ((LehrmaterialController) layout.getController()).setModus("Lehrmaterial");

    }

    public void uebersichtsseiteAufrufen(Object nutzer, Lehrveranstaltung lehrveranstaltung) {
        this.nutzer = nutzer;
        this.lehrveranstaltung= lehrveranstaltung;

        if (nutzer != null) {
            if (nutzer instanceof Lehrender) {
                title.setText(((Lehrveranstaltung) lehrveranstaltung).getTitel());
               // materialUpload.setText("Lehrmaterial hochladen")
                getMaterial((Lehrveranstaltung) lehrveranstaltung);

            }
            else if(nutzer instanceof Student) {
                title.setText(((Lehrveranstaltung) lehrveranstaltung).getTitel());

                materialUpload.setVisible(false);
                studentenliste.setVisible(false);
                getMaterial((Lehrveranstaltung) lehrveranstaltung);
            }
        }
    }

    public void projektgruppePressedButton(ActionEvent actionEvent) {
        actionEvent.consume();
        layout.instanceLayout("projektgruppenliste.fxml");
        ((ProjektgruppenController) layout.getController()).setLayout(layout);
        ((ProjektgruppenController) layout.getController()).setNutzer(nutzer);
        ((ProjektgruppenController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
        ((ProjektgruppenController) layout.getController()).populateTableView();
        ((ProjektgruppenController) layout.getController()).setPGListeSeitentitel(lehrveranstaltung.getTitel());

    }

    public void quizPressed(ActionEvent actionEvent) {
        layout.instanceLayout("quizUebersicht.fxml");
        ((QuizUebersichtController) layout.getController()).setLayout(layout);
        ((QuizUebersichtController) layout.getController()).quizSeiteAufrufen(nutzer,lehrveranstaltung);
        ((QuizUebersichtController) layout.getController()).quizerstellen_LvTitel_Label.setText("Lehrveranstaltung " + lehrveranstaltung.getTitel() );
    }

    public void reviewErstellenPressedButton(ActionEvent actionEvent) {
    }

    public void reviewStatistikPressedBtn(ActionEvent actionEvent) {
    }

    public void reviewPressedButton(ActionEvent actionEvent) {
    }
}