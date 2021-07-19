package Client.Controller.Quiz;

import Client.Controller.Lehrveranstaltung.LehrveranstaltungsuebersichtsseiteController;
import Client.Layouts.Layout;
import Client.Modell.Lehrender;
import Client.Modell.Lehrveranstaltung;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
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
import java.util.List;

public class QuizXMLUploadController {
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
    private Lehrveranstaltung lehrveranstaltung;
    private Object nutzerInstanz;
    private String modus;

    private Layout layout;

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
        setNutzerInstanz(layout.getNutzer());
    }

    public void initialize() {

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
//            System.out.println("Dateiauswahl wurde abgebrochen.");
        }

    }

    public void hochladenPressedButton(ActionEvent actionEvent) {
        actionEvent.consume();
        long id = ((Lehrender) nutzerInstanz).getId();
            if (fileList != null) {
                try (CloseableHttpClient client = HttpClients.createDefault()) {

                    String url = "http://localhost:8080/quiz/xmlQuiz";
                    HttpPost post = new HttpPost(url);
                    MultipartEntityBuilder entity = MultipartEntityBuilder.create();

                    for (File file : fileList) {
                        entity.addPart("files", new FileBody(file));
                    }
                    entity.addTextBody("lehrenderId", String.valueOf(id));
                    entity.addTextBody("lehrveranstaltungsId", String.valueOf(((Lehrveranstaltung) lehrveranstaltung).getId()));
                    HttpEntity requestEntity = entity.build();
                    post.setEntity(requestEntity);

                    try (CloseableHttpResponse response = client.execute(post)) {
                        HttpEntity responseEntity = response.getEntity();
                        String result = EntityUtils.toString(responseEntity);
                        if(result.equals("OK")) {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Erfolgreich hochgeladen!");
                            alert.setHeaderText("Ihre XML Datei wurde erfolgreich zum Server hochgeladen!");
                            alert.setContentText("Sie werden nun zur Quiz Übersichtsseite weitergeleitet.");
                            alert.showAndWait();
                            layout.instanceLayout("quizUebersicht.fxml");
                            ((QuizUebersichtController) layout.getController()).setLayout(layout);
                            ((QuizUebersichtController) layout.getController()).quizSeiteAufrufen(nutzerInstanz, lehrveranstaltung);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
//                System.out.println("Keine Datei zum Hochladen ausgewählt!");
            }
    }

    public void abbrechenPressedButton(ActionEvent actionEvent) {
            layout.instanceLayout("lehrveranstaltungsuebersichtsseite.fxml");
            ((LehrveranstaltungsuebersichtsseiteController) layout.getController()).setLayout(layout);
            ((LehrveranstaltungsuebersichtsseiteController) layout.getController()).uebersichtsseiteAufrufen(nutzerInstanz,lehrveranstaltung);
    }

    public Object getLehrveranstaltung() {
        return lehrveranstaltung;
    }

    public void setLehrveranstaltung(Lehrveranstaltung lehrveranstaltung) {
        this.lehrveranstaltung = lehrveranstaltung;
    }

    public Object getNutzerInstanz() {
        return nutzerInstanz;
    }

    public void setNutzerInstanz(Object nutzerInstanz) {
        this.nutzerInstanz = nutzerInstanz;
    }
}
