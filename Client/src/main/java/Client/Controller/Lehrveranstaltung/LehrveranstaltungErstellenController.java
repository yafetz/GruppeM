package Client.Controller.Lehrveranstaltung;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Client.Controller.Kurse.MeineKurseController;
import Client.Layouts.Layout;
import Client.Modell.Lehrender;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class LehrveranstaltungErstellenController {
    @FXML
    private TextField titels;
    @FXML
    private TextField semesters;
    @FXML
    private Button erstellen;
    @FXML
    private Button addCSV;
    @FXML
    private ChoiceBox<String> typVorlesung;

    private Object nutzerInstanz;

    ObservableList<String> options = FXCollections.observableArrayList("Vorlesung","Seminar");

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

    public void uebersichtsseiteAufrufen(Object nutzer) {
        this.nutzerInstanz = nutzer;
    }

    @FXML
    private void erstellenPressedButton(ActionEvent event) {
        event.consume();
        long nutzerId = ((Lehrender) nutzerInstanz).getNutzerId().getId();
        String tit = titels.getText();
        String split = tit.replaceAll(" ", "%20");
        String veranstaltungstyp = typVorlesung.getValue();
        String sem = semesters.getText();
        String splitsem = sem.replaceAll(" ", "%20");
        String newsplitsem = splitsem.replaceAll("/","%2F");
        Pattern pattern1 = Pattern.compile("sose");
        Pattern pattern2 = Pattern.compile("wise");
        Matcher matcher1 = pattern1.matcher(sem.toLowerCase());
        Matcher matcher2 = pattern2.matcher(sem.toLowerCase());
        boolean matchFound = matcher1.find();
        boolean matchFound2 = matcher2.find();
        if(matchFound || matchFound2) {
            try (CloseableHttpClient client1 = HttpClients.createDefault()) {

                String url = "http://localhost:8080/lehrveranstaltung/create/lehrveranstaltung/";
                HttpPost post = new HttpPost(url);
                MultipartEntityBuilder entity = MultipartEntityBuilder.create();
                entity.setCharset(StandardCharsets.UTF_8);
                entity.addTextBody("titel",tit,ContentType.create("text/plain", MIME.UTF8_CHARSET));
                entity.addTextBody("lehrenderd",String.valueOf(nutzerId), ContentType.create("text/plain", MIME.UTF8_CHARSET));
                entity.addTextBody("art",veranstaltungstyp,ContentType.create("text/plain", MIME.UTF8_CHARSET));
                entity.addTextBody("semester",sem,ContentType.create("text/plain", MIME.UTF8_CHARSET));

                HttpEntity requestEntity = entity.build();
                post.setEntity(requestEntity);

                try (CloseableHttpResponse response1 = client1.execute(post)) {
                    HttpEntity responseEntity = response1.getEntity();
                    String result = EntityUtils.toString(responseEntity);
                    layout.instanceLayout("meineKurse.fxml");
                    ((MeineKurseController) layout.getController()).setLayout(layout);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
//            System.out.println("Falsches Semester");
        }
    }

        public Object getNutzerInstanz () {
            return nutzerInstanz;
        }
        public void setNutzerInstanz (Object nutzerInstanz){

        this.nutzerInstanz = nutzerInstanz;
        typVorlesung.setItems(options);
    }

    public void AddCsv(ActionEvent actionEvent) {
        layout.instanceLayout("lehrmaterialUpload.fxml");
        ((LehrmaterialController) layout.getController()).setLayout(layout);
        ((LehrmaterialController) layout.getController()).setModus("CSV");
    }
}

