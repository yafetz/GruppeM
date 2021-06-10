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

    public void Studenliste(ActionEvent actionEvent) {
        Layout lehrveranstaltungBeitreten = new Layout("studentenListe.fxml", (Stage) teilnehmerListe.getScene().getWindow(),nutzer);
        if(lehrveranstaltungBeitreten.getController() instanceof StudentenListeController){

            ((StudentenListeController) lehrveranstaltungBeitreten.getController()).setNutzerInstanz(nutzer);
            ((StudentenListeController) lehrveranstaltungBeitreten.getController()).setLehrveranstaltung(lehrveranstaltung);

        }
    }

    @FXML
    private void teilnehmerListe(ActionEvent event){

        Layout lehrveranstaltungBeitreten = new Layout("teilnehmerListe.fxml", (Stage) teilnehmerListe.getScene().getWindow(),nutzer);
        if(lehrveranstaltungBeitreten.getController() instanceof TeilnehmerListeController){
            long veranstaltungId = ((Lehrveranstaltung) lehrveranstaltung).getId();

            ((TeilnehmerListeController) lehrveranstaltungBeitreten.getController()).setId(veranstaltungId);
            ((TeilnehmerListeController) lehrveranstaltungBeitreten.getController()).setNutzerInstanz(nutzer);
            ((TeilnehmerListeController)  lehrveranstaltungBeitreten.getController()).setLehrveranstaltung(((Lehrveranstaltung) lehrveranstaltung));
        }
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
        Stage stage = (Stage) materialUpload.getScene().getWindow();
        Layout uploadScreen = null;
        uploadScreen = new Layout("lehrmaterialUpload.fxml", stage,nutzer);
        if (uploadScreen.getController() instanceof LehrmaterialController) {
            ((LehrmaterialController) uploadScreen.getController()).setNutzerInstanz(nutzer);
            ((LehrmaterialController) uploadScreen.getController()).setLehrveranstaltung(lehrveranstaltung);
            ((LehrmaterialController) uploadScreen.getController()).setModus("Lehrmaterial");
        }

    }

    public void uebersichtsseiteAufrufen(Object nutzer, Lehrveranstaltung lehrveranstaltung) {
        this.nutzer = nutzer;
        this.lehrveranstaltung= lehrveranstaltung;


        if (nutzer !=null) {
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
}