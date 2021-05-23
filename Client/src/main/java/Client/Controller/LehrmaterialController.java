package Client.Controller;

import Client.Layouts.Layout;
import Client.Modell.Lehrender;
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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;
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
import java.util.List;

public class LehrmaterialController {

    @FXML
    public Label uploadSeiteLabel;
    @FXML
    private Button btn_upload;
    @FXML
    private Button btn_durchsuchen;
    @FXML
    public Button btn_abbrechen;
    @FXML
    private ListView listview_upload;

    private List<File> fileList;
    private ObservableList<File> obsFileList;
    private Object lehrveranstaltung;
    private Object nutzerInstanz;
    private String modus;

    public void initialize() {

    }

    public void initializePageLabel() {
        if (modus.equals("Lehrmaterial")) {
            this.uploadSeiteLabel.setText("Lehrmaterial hochladen");
        } else if (modus.equals("CSV")) {
            this.uploadSeiteLabel.setText("CSV-Datei hochladen");
        }
    }

    public void durchsuchenPressedButton(ActionEvent actionEvent) {
        actionEvent.consume();
        Stage stage = (Stage) btn_durchsuchen.getScene().getWindow();
        FileChooser filechooser = new FileChooser();
        filechooser.setTitle("Datei auswählen");
        fileList = filechooser.showOpenMultipleDialog(stage);
        try {
            obsFileList = FXCollections.observableList(fileList);
            listview_upload.setItems(obsFileList);
        } catch (Exception e) {
            System.out.println("Dateiauswahl wurde abgebrochen.");
        }

    }

    public void hochladenPressedButton(ActionEvent actionEvent) {
        actionEvent.consume();
        long id = ((Lehrender)nutzerInstanz).getId();
        if (modus.equals("Lehrmaterial")) {
            if (fileList != null) {
                try (CloseableHttpClient client = HttpClients.createDefault()) {

                    String url = "http://localhost:8080/lehrmaterial/upload";
                    HttpPost post = new HttpPost(url);
                    MultipartEntityBuilder entity = MultipartEntityBuilder.create();

                    for (File file : fileList) {
                        entity.addPart("files", new FileBody(file));
                    }

                    entity.addTextBody("lehrveranstaltungId", String.valueOf(((Lehrveranstaltung) lehrveranstaltung).getId()));
                    HttpEntity requestEntity = entity.build();
                    post.setEntity(requestEntity);

                    try (CloseableHttpResponse response = client.execute(post)) {
                        HttpEntity responseEntity = response.getEntity();
                        String result = EntityUtils.toString(responseEntity);
                        System.out.println(result);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Keine Datei zum Hochladen ausgewählt!");
            }
        } else if (modus.equals("CSV")) {
            if (fileList != null) {
                try (CloseableHttpClient client = HttpClients.createDefault()) {

                    String url = "http://localhost:8080/lehrmaterial/csv";
                    HttpPost post = new HttpPost(url);
                MultipartEntityBuilder entity = MultipartEntityBuilder.create();
                int countCsv = 0;
                for (File file : fileList) {
                    if(FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("csv")){
                        entity.addPart("files", new FileBody(file));
                        System.out.println(file.getName());
                        countCsv++;
                    }
                }
                    if(countCsv > 0) {
                        entity.addTextBody("nutzerId", String.valueOf(((Lehrender) nutzerInstanz).getNutzerId().getId()));
                        HttpEntity requestEntity = entity.build();
                        post.setEntity(requestEntity);
                    try (CloseableHttpResponse response = client.execute(post)) {
                        HttpEntity responseEntity = response.getEntity();
                        String result = EntityUtils.toString(responseEntity);
                        Layout meineKurse = new Layout("meineKurse.fxml",(Stage) btn_upload.getScene().getWindow(),nutzerInstanz);
                        if(meineKurse.getController() instanceof MeineKurseController){
                            ((MeineKurseController) meineKurse.getController()).setNutzerInstanz(nutzerInstanz);
                        }
                    }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void abbrechenPressedButton(ActionEvent actionEvent) {
        actionEvent.consume();
        Stage stage = (Stage) btn_abbrechen.getScene().getWindow();
        Layout homeScreen = null;
        if (modus.equals("Lehrmaterial")) {
            homeScreen = new Layout("lehrveranstaltungsuebersichtsseite.fxml", stage, nutzerInstanz);
            if (homeScreen.getController() instanceof LehrveranstaltungsuebersichtsseiteController) {
                ((LehrveranstaltungsuebersichtsseiteController) homeScreen.getController()).uebersichtsseiteAufrufen(nutzerInstanz, lehrveranstaltung);
            }
        } else if (modus.equals("CSV")) {
            homeScreen = new Layout("meineKurse.fxml", stage, nutzerInstanz);
            if (homeScreen.getController() instanceof MeineKurseController) {
                ((MeineKurseController) homeScreen.getController()).setNutzerInstanz(nutzerInstanz);
            }
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

    public String getModus() {
        return modus;
    }

    public void setModus(String modus) {
        this.modus = modus;
    }
}
