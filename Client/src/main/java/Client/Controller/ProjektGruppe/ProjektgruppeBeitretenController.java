package Client.Controller.ProjektGruppe;

import Client.Layouts.Layout;
import Client.Modell.Lehrveranstaltung;
import Client.Modell.Projektgruppe;
import Client.Modell.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

public class ProjektgruppeBeitretenController {

    @FXML private Label beitretenPgTitel_label;
    @FXML private Label beitretenLvTitel_label;
    @FXML private Button beitreten_btn;
    @FXML private Button zurueck_btn;

    private Object nutzer;
    private Lehrveranstaltung lehrveranstaltung;
    private Projektgruppe projektgruppe;

    private Layout layout;

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
        beitretenLvTitel_label.setText(lehrveranstaltung.getTitel());
    }

    public Projektgruppe getProjektgruppe() {
        return projektgruppe;
    }

    public void setProjektgruppe(Projektgruppe projektgruppe) {
        this.projektgruppe = projektgruppe;
        beitretenPgTitel_label.setText(projektgruppe.getTitel());
    }

    public void beitretenPressedButton(ActionEvent actionEvent) {
        actionEvent.consume();
        long studentId = -1;
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            if (nutzer instanceof Student) {
                studentId = ((Student) nutzer).getId();
            }
            String url = "http://localhost:8080/projektgruppe/newMember";
            HttpPost post = new HttpPost(url);
            MultipartEntityBuilder entity = MultipartEntityBuilder.create();
            entity.setCharset(StandardCharsets.UTF_8);
            entity.addTextBody("projektgruppeId", String.valueOf(projektgruppe.getId()), ContentType.create("text/plain", MIME.UTF8_CHARSET));
            entity.addTextBody("studentId", String.valueOf(studentId), ContentType.create("text/plain", MIME.UTF8_CHARSET));
            HttpEntity requestEntity = entity.build();
            post.setEntity(requestEntity);
            try (CloseableHttpResponse response = client.execute(post)) {
                HttpEntity responseEntity = response.getEntity();
                String responseBody = EntityUtils.toString(responseEntity);

                //Layout projektgruppeUebersicht = new Layout("projektgruppeUebersicht.fxml", (Stage) beitreten_btn.getScene().getWindow(), nutzer);
                layout.instanceLayout("projektgruppeUebersicht.fxml");
                ((ProjektgruppenController) layout.getController()).setLayout(layout);
                if (layout.getController() instanceof ProjektgruppenController) {
                    ((ProjektgruppenController) layout.getController()).setNutzer(nutzer);
                    ((ProjektgruppenController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
                    ((ProjektgruppenController) layout.getController()).setProjektgruppe(projektgruppe);
                    ((ProjektgruppenController) layout.getController()).setPGUebersichtLvTitel(lehrveranstaltung.getTitel());
                    ((ProjektgruppenController) layout.getController()).setPGUebersichtPGTitel(projektgruppe.getTitel());
                    ((ProjektgruppenController) layout.getController()).setChautraumId((int) projektgruppe.getChat().getId());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // anklicken von "Zurück zur Projektgruppenliste"-Button auf der Projektgruppenbeitrittsseite
    public void zurueckPressedButton(ActionEvent actionEvent) {
        actionEvent.consume();
        //Layout projektgruppenliste = new Layout ("projektgruppenliste.fxml", (Stage) zurueck_btn.getScene().getWindow(), nutzer);
        layout.instanceLayout("projektgruppenliste.fxml");
        ((ProjektgruppenController) layout.getController()).setLayout(layout);
        if (layout.getController() instanceof ProjektgruppenController) {
            ((ProjektgruppenController) layout.getController()).setNutzer(nutzer);
            ((ProjektgruppenController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
            ((ProjektgruppenController) layout.getController()).populateTableView();
            ((ProjektgruppenController) layout.getController()).setPGListeSeitentitel(lehrveranstaltung.getTitel());
        }
    }
}
