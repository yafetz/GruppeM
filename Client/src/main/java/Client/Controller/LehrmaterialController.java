package Client.Controller;

import Client.Modell.Lehrveranstaltung;
import Client.Modell.Nutzer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class LehrmaterialController {

    @FXML
    private Button meineKurse;
    @FXML
    private Button alleKurse;
    @FXML
    private Hyperlink namenLink;
    @FXML
    private ImageView profilBild;
    @FXML
    private Button btn_upload;
    @FXML
    private Button btn_durchsuchen;
    @FXML
    private ListView listview_upload;

    private List<File> fileList;
    private ObservableList<File> obsFileList;
    private Object lehrveranstaltung;
    private Object nutzerInstanz;

    public void initialize() {

    }

    public void meineKurseAufrufen(ActionEvent event) {
        event.consume();
        Stage stage = (Stage) meineKurse.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("meineKurse.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            MeineKurseController meineKurse = loader.getController();
            Scene scene = new Scene(root);
            String homescreencss = getClass().getClassLoader().getResource("css/login.css").toExternalForm();
            scene.getStylesheets().add(homescreencss);
            stage.setScene(scene);
            stage.setMaximized(false);
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void alleKurseAufrufen(ActionEvent event) {
        HomescreenController homescreenController = new HomescreenController();
        homescreenController.alleKurseAufrufen(event);
//        event.consume();
//        Stage stage = (Stage) alleKurse.getScene().getWindow();
//        try {
//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(getClass().getClassLoader().getResource("alleKurse.fxml"));
//            AnchorPane root = (AnchorPane) loader.load();
//            AlleKurseController alleKurse = loader.getController();
//            Scene scene = new Scene(root);
//            String homescreencss = getClass().getClassLoader().getResource("css/login.css").toExternalForm();
//            scene.getStylesheets().add(homescreencss);
//            stage.setScene(scene);
//            stage.setMaximized(false);
//            stage.show();
//        } catch (IOException e){
//            e.printStackTrace();
//        }
    }

    public void eigeneProfilSeiteAufrufen(ActionEvent event) {
        event.consume();
        Stage stage = (Stage) namenLink.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("userprofile.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            UserprofilController userprofil = loader.getController();
            Scene scene = new Scene(root);
            String homescreencss = getClass().getClassLoader().getResource("css/login.css").toExternalForm();
            scene.getStylesheets().add(homescreencss);
            stage.setScene(scene);
            stage.setMaximized(false);
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void durchsuchenPressedButton(ActionEvent actionEvent) throws IOException {
        actionEvent.consume();
        Stage stage = (Stage) btn_durchsuchen.getScene().getWindow();
        FileChooser filechooser = new FileChooser();
        filechooser.setTitle("Datei auswählen");
        fileList = filechooser.showOpenMultipleDialog(stage);
        obsFileList = FXCollections.observableList(fileList);
        listview_upload.setItems(obsFileList);
    }

    public void hochladenPressedButton(ActionEvent actionEvent) {
        actionEvent.consume();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.addAll("files", fileList);

//        System.out.println(body);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        if(lehrveranstaltung instanceof Lehrveranstaltung) {

            System.out.println("instanceof test erfolgreich");

            String url = "http://localhost:8080/lehrveranstaltung/lehrmaterial/upload/" + ((Lehrveranstaltung) lehrveranstaltung).getId();
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
            System.out.println(responseEntity.getBody());
        }


    }

    public Object getLehrveranstaltung() {
        return lehrveranstaltung;
    }

    public void setLehrveranstaltung(Object lehrveranstaltung) {
        this.lehrveranstaltung = lehrveranstaltung;
    }

    public Object getNutzerInstanz() {
        return nutzerInstanz;
    }

    public void setNutzerInstanz(Object nutzerInstanz) {
        this.nutzerInstanz = nutzerInstanz;
    }
}
