package Client.Controller.Thema;

import Client.Controller.Lehrveranstaltung.LehrveranstaltungsuebersichtsseiteController;
import Client.Layouts.Layout;
import Client.Modell.Lehrveranstaltung;
import Client.Modell.Thema;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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

public class LiteraturUploadController {
    public Button btn_durchsuchen;
    public Button btn_upload;
    public ListView listview_upload;
    public Button btn_abbrechen;

    public Layout layout;
    private List<File> fileList;
    private ObservableList<File> obsFileList;
    public Thema thema;

    public void setThema(Thema thema){
        this.thema = thema;
    }

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public void abbrechenPressedButton(ActionEvent actionEvent) {
        layout.instanceLayout("LiteraturHinzufügen.fxml");
        ((LiteraturHinzufügenController) layout.getController()).setLayout(layout);
        ((LiteraturHinzufügenController) layout.getController()).setThema(thema);
    }

    public void hochladenPressedButton(ActionEvent actionEvent) {
        if (fileList != null) {
            try (CloseableHttpClient client = HttpClients.createDefault()) {

                String url = "http://localhost:8080/themen/literatur/upload";
                HttpPost post = new HttpPost(url);
                MultipartEntityBuilder entity = MultipartEntityBuilder.create();

                for (File file : fileList) {
                    entity.addPart("files", new FileBody(file));
                }
                HttpEntity requestEntity = entity.build();
                post.setEntity(requestEntity);

                try (CloseableHttpResponse response = client.execute(post)) {
                    HttpEntity responseEntity = response.getEntity();
                    String result = EntityUtils.toString(responseEntity);
                    if(result.equals("OK")) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Erfolgreich hochgeladen!");
                        alert.setHeaderText("Ihre Bibtex Datei wurde erfolgreich zum Server hochgeladen!");
                        alert.setContentText("Sie werden nun zur Literatur hinzufügen Seite weitergeleitet.");
                        alert.showAndWait();
                        layout.instanceLayout("LiteraturHinzufügen.fxml");
                        ((LiteraturHinzufügenController) layout.getController()).setLayout(layout);
                        ((LiteraturHinzufügenController) layout.getController()).setThema(thema);
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void durchsuchenPressedButton(ActionEvent actionEvent) {
        FileChooser filechooser = new FileChooser();
        filechooser.setTitle("Datei auswählen");
        Stage stage = (Stage) btn_durchsuchen.getScene().getWindow();
        fileList = filechooser.showOpenMultipleDialog(stage);
        try {
            obsFileList = FXCollections.observableList(fileList);
            listview_upload.setItems(obsFileList);
        } catch (Exception e) {
        }
    }
}
