package Client.Controller.Lehrveranstaltung;

import Client.Controller.Lehrveranstaltung.LehrveranstaltungsuebersichtsseiteController;
import Client.Layouts.Layout;
import Client.Modell.Lehrender;
import Client.Modell.Lehrveranstaltung;
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
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LehrveranstaltungBearbeitenController {

    @FXML
    private TextField titel;
    @FXML
    private TextField semesters;
    @FXML
    private Button finished;
    @FXML
    private ChoiceBox<String> typVorlesung;
    private Object nutzer;
    ObservableList<String> options = FXCollections.observableArrayList("Vorlesung","Seminar");

    private Layout layout;
    private Lehrveranstaltung lehrveranstaltung;

    public Object getNutzer() {
        return nutzer;
    }

    public void setNutzer(Object nutzer) {
        this.nutzer = nutzer;
        titel.setText(lehrveranstaltung.getTitel());
        semesters.setText(lehrveranstaltung.getSemester());
        typVorlesung.setValue(lehrveranstaltung.getArt());
        typVorlesung.setItems(options);
    }

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public Lehrveranstaltung getLehrveranstaltung() {
        return lehrveranstaltung;
    }

    public void setLehrveranstaltung(Lehrveranstaltung lehrveranstaltung) {
        this.lehrveranstaltung = lehrveranstaltung;
    }

    public TextField getTitel() {
        return titel;
    }

    public void setTitel(TextField titel) {
        this.titel = titel;
    }

    public TextField getSemesters() {
        return semesters;
    }

    public void setSemesters(TextField semesters) {
        this.semesters = semesters;
    }

    public Button getFinished() {
        return finished;
    }

    public void setFinished(Button finished) {
        this.finished = finished;
    }

    public ChoiceBox getTypVorlesung() {
        return typVorlesung;
    }

    public void setTypVorlesung(ChoiceBox typVorlesung) {
        this.typVorlesung = typVorlesung;
    }


    public void bearbeitenPressedButton(ActionEvent actionEvent) {
        actionEvent.consume();
        long nutzerId = ((Lehrender) nutzer).getNutzerId().getId();
        String tit = titel.getText();
        String split = tit.replaceAll(" ", "%20");
        String veranstaltungstyp = typVorlesung.getValue();
        String sem = semesters.getText();
        String splitsem = sem.replaceAll(" ", "%20");
        String newsplitsem = splitsem.replaceAll("/", "%2F");
        Pattern pattern1 = Pattern.compile("sose");
        Pattern pattern2 = Pattern.compile("wise");
        Matcher matcher1 = pattern1.matcher(sem.toLowerCase());
        Matcher matcher2 = pattern2.matcher(sem.toLowerCase());
        boolean matchFound = matcher1.find();
        boolean matchFound2 = matcher2.find();
        if (matchFound || matchFound2) {
            try (CloseableHttpClient client1 = HttpClients.createDefault()) {

                String url = "http://localhost:8080/lehrveranstaltung/update/lehrveranstaltung/";
                HttpPost post = new HttpPost(url);
                MultipartEntityBuilder entity = MultipartEntityBuilder.create();
                entity.setCharset(StandardCharsets.UTF_8);
                entity.addTextBody("titel", tit, ContentType.create("text/plain", MIME.UTF8_CHARSET));
                entity.addTextBody("lehrenderd", String.valueOf(nutzerId), ContentType.create("text/plain", MIME.UTF8_CHARSET));
                entity.addTextBody("art", veranstaltungstyp, ContentType.create("text/plain", MIME.UTF8_CHARSET));
                entity.addTextBody("semester", sem, ContentType.create("text/plain", MIME.UTF8_CHARSET));
                entity.addTextBody("id", String.valueOf(lehrveranstaltung.getId()));

                HttpEntity requestEntity = entity.build();
                post.setEntity(requestEntity);
                try (CloseableHttpResponse response1 = client1.execute(post)) {
                    HttpEntity responseEntity = response1.getEntity();
                    String result = EntityUtils.toString(responseEntity);
                    layout.instanceLayout("lehrveranstaltungsuebersichtsseite.fxml");
                    ((LehrveranstaltungsuebersichtsseiteController) layout.getController()).setLayout(layout);
                    ((LehrveranstaltungsuebersichtsseiteController) layout.getController()).uebersichtsseiteAufrufen(nutzer,lehrveranstaltung);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
