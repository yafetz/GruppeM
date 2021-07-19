package Client.Controller.Gruppenmitglieder;

import Client.Controller.ProjektGruppe.ProjektgruppenController;
import Client.Layouts.Layout;
import Client.Modell.Projektgruppe;
import Client.Modell.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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

public class GruppenUploadController {

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
    private Projektgruppe projektgruppe;
    private Object nutzerInstanz;
    private String modus;

    private Layout layout;

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
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
        long id = ((Student) nutzerInstanz).getNutzer().getId();

            if (fileList != null) {
                try (CloseableHttpClient client = HttpClients.createDefault()) {

                    String url = "http://localhost:8080/gruppenmaterial/upload";
                    HttpPost post = new HttpPost(url);
                    MultipartEntityBuilder entity = MultipartEntityBuilder.create();

                    for (File file : fileList) {
                        entity.addPart("files", new FileBody(file));
                    }

                    entity.addTextBody("gruppenId", String.valueOf(((Projektgruppe) projektgruppe).getId()));
                    HttpEntity requestEntity = entity.build();
                    post.setEntity(requestEntity);

                    try (CloseableHttpResponse response = client.execute(post)) {
                        HttpEntity responseEntity = response.getEntity();
                        String result = EntityUtils.toString(responseEntity);
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Erfolgreich hochgeladen!");
                        alert.setHeaderText("Ihre Lehrmaterialien wurden erfolgreich zum Server hochgeladen!");
                        alert.setContentText("Sie werden nun zur Übersichtsseite weitergeleitet.");
                        alert.showAndWait();
                        layout.instanceLayout("projektgruppeUebersicht.fxml");
                        if (layout.getController() instanceof ProjektgruppenController) {
                            ((ProjektgruppenController) layout.getController()).setNutzer(nutzerInstanz);
                            ((ProjektgruppenController) layout.getController()).setProjektgruppe(projektgruppe);
                            ((ProjektgruppenController) layout.getController()).setLehrveranstaltung(projektgruppe.getLehrveranstaltung());
                            ((ProjektgruppenController) layout.getController()).setPGUebersichtLvTitel(projektgruppe.getLehrveranstaltung().getTitel());
                            ((ProjektgruppenController) layout.getController()).setPGUebersichtPGTitel(projektgruppe.getTitel());
                            ((ProjektgruppenController) layout.getController()).setLayout(layout);
                            ((ProjektgruppenController) layout.getController()).populateMaterialTable();
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
        actionEvent.consume();
        Stage stage = (Stage) btn_abbrechen.getScene().getWindow();
        Layout homeScreen = null;

    }

    public Object getProjektgruppe() {
        return projektgruppe;
    }

    public void setProjektgruppe(Projektgruppe projektgruppe) {
        this.projektgruppe = projektgruppe;
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
        if (modus.equals("Lehrmaterial")) {
            this.uploadSeiteLabel.setText("Lehrmaterial hochladen");
        } else if (modus.equals("CSV")) {
            this.uploadSeiteLabel.setText("CSV-Datei hochladen");
        }
    }
}
