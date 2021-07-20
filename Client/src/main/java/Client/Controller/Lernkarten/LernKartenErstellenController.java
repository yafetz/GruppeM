package Client.Controller.Lernkarten;

import Client.Layouts.Layout;
import Client.Modell.Lehrveranstaltung;
import Client.Modell.Lernkartenset;
import Client.Modell.Projektgruppe;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

public class LernKartenErstellenController {

    @FXML
    public Button back;
    @FXML
    public Button neuekarte;
    @FXML
    public Label info;
    @FXML
    public TextField frage_textfield;
    @FXML
    public TextField antwort_textfield;

    private Layout layout;
    private Object nutzer;
    private Lehrveranstaltung lehrveranstaltung;
    private Projektgruppe projektgruppe;
    private Lernkartenset lernkartenset;

    public void ActionNeuekarte() {

        if(frage_textfield.getText().length() == 0) {
            info.setText("Fehler: Du musst eine Frage eingeben.");
            return;
        }

        if(antwort_textfield.getText().length() == 0) {
            info.setText("Fehler: Du musst eine Antwort eingeben.");
            return;
        }

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost("http://localhost:8080/lernkarten/createLernkarte");
        HttpEntity entity = MultipartEntityBuilder.create()

                .setCharset(StandardCharsets.UTF_8)

                .addTextBody("lernkartenset_id", String.valueOf(lernkartenset.getId()), ContentType.create("text/plain", MIME.UTF8_CHARSET))
                .addTextBody("frage", frage_textfield.getText(), ContentType.create("text/plain", MIME.UTF8_CHARSET))
                .addTextBody("antwort", antwort_textfield.getText(), ContentType.create("text/plain", MIME.UTF8_CHARSET))
                .build();
        post.setEntity(entity);

        try {

            CloseableHttpResponse response = client.execute(post);
            HttpEntity responseEntity = response.getEntity();
            String responseBody = EntityUtils.toString(responseEntity);
            info.setText(responseBody);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        frage_textfield.setText("");
        antwort_textfield.setText("");
    }

    public void ActionBack() {
        layout.instanceLayout("lernkarteAnsicht.fxml");
        ((LernkartenController) layout.getController()).setLayout(layout);
        ((LernkartenController) layout.getController()).setProjektgruppe(projektgruppe);
        ((LernkartenController) layout.getController()).setNutzer(nutzer);
        ((LernkartenController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
        ((LernkartenController) layout.getController()).setLernkartenset(lernkartenset);
        ((LernkartenController) layout.getController()).initLernkartenController(lernkartenset.getId());
    }

    public void initLernKartenErstellen() {

        frage_textfield.setText("");
        antwort_textfield.setText("");
        info.setText("");
    }


    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
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

    public Projektgruppe getProjektgruppe() {
        return projektgruppe;
    }

    public void setProjektgruppe(Projektgruppe projektgruppe) {
        this.projektgruppe = projektgruppe;
    }

    public Lernkartenset getLernkartenset() {
        return lernkartenset;
    }

    public void setLernkartenset(Lernkartenset lernkartenset) {
        this.lernkartenset = lernkartenset;
    }
}
