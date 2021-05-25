package Client.Controller;

import Client.Layouts.Auth;
import Client.Layouts.Layout;
import Client.Modell.Lehrender;
import Client.Modell.Student;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class EditierenController {
    @FXML
    private TextField passwort;
    @FXML
    private TextField stadt;
    @FXML
    private TextField straße;
    @FXML
    private TextField hausnummer;
    @FXML
    private TextField postleitzahl;
    @FXML
    private TextField lehrstuhl;
    @FXML
    private TextField forschungsgebiet;
    @FXML
    private TextField fach;
    @FXML
    private Text lehrstuhltext;
    @FXML
    private Text forschungstext;
    @FXML
    private Button aktualisieren;
    @FXML
    private Button abbrechen;

    private Object Nutzer;
    @FXML
    private ImageView profilbild;
    @FXML
    private Text fachtext;

    private File profil;

    public void initialize(){
        profilbild.setCursor(Cursor.HAND);
        profilbild.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                event.consume();
                FileChooser filechooser = new FileChooser();
                filechooser.setTitle("Datei auswählen");
                profil = filechooser.showOpenDialog(profilbild.getScene().getWindow());
                if(profil != null) {
                    profilbild.setImage(new Image(profil.toURI().toString(), 200, 200, true, true));
                }
            }
        });
    }

    public Object getNutzer() {
        return Nutzer;
    }

    public void setNutzer(Object nutzer) {
        Nutzer = nutzer;
        if(nutzer instanceof Lehrender){
            Lehrender l = (Lehrender) nutzer;
            passwort.setText(l.getNutzerId().getPasswort());
            stadt.setText(l.getNutzerId().getStadt());
            straße.setText(l.getNutzerId().getStrasse());
            hausnummer.setText(String.valueOf(l.getNutzerId().getHausnummer()));
            postleitzahl.setText(String.valueOf(l.getNutzerId().getPlz()));
            fach.setVisible(false);
            fachtext.setVisible(false);
            lehrstuhl.setText(l.getLehrstuhl());
            forschungsgebiet.setText(l.getForschungsgebiet());
        } else if (nutzer instanceof Student) {
            Student s = (Student) nutzer;
            passwort.setText(s.getNutzer().getPasswort());
            stadt.setText(s.getNutzer().getStadt());
            straße.setText(s.getNutzer().getStrasse());
            hausnummer.setText(String.valueOf(s.getNutzer().getHausnummer()));
            postleitzahl.setText(String.valueOf(s.getNutzer().getPlz()));
            lehrstuhl.setVisible(false);
            lehrstuhltext.setVisible(false);
            forschungsgebiet.setVisible(false);
            forschungstext.setVisible(false);
            fach.setText(s.getStudienfach());
        }
    }

    public void Nutzerprofil_veraendern(ActionEvent actionEvent) {
        actionEvent.consume();
        if(Nutzer instanceof Lehrender){
            String newLehrstuhl = lehrstuhl.getText().trim();
            newLehrstuhl = newLehrstuhl.replaceAll(" ", "%20");
            String newForschungsgebiet = forschungsgebiet.getText().trim();
            newForschungsgebiet = newForschungsgebiet.replaceAll(" ", "%20");
            String newStadt = stadt.getText().trim();
            newStadt = newStadt.replaceAll(" ", "%20");
            String newStraße = straße.getText().trim();
            newStraße = newStraße.replaceAll(" ", "%20");
            try (CloseableHttpClient client1 = HttpClients.createDefault()) {

                String url = "http://localhost:8080/register/update/lehrender";
                HttpPost post = new HttpPost(url);
                MultipartEntityBuilder entity = MultipartEntityBuilder.create();
                entity.setCharset(StandardCharsets.UTF_8);
                entity.addTextBody("nutzerId", String.valueOf(((Lehrender) Nutzer).getNutzerId().getId()));
                entity.addTextBody("passwort",passwort.getText(),ContentType.create("text/plain", MIME.UTF8_CHARSET));
                entity.addTextBody("lehrstuhl",newLehrstuhl,ContentType.create("text/plain", MIME.UTF8_CHARSET));
                entity.addTextBody("forschungsgebiet",newForschungsgebiet,ContentType.create("text/plain", MIME.UTF8_CHARSET));
                entity.addTextBody("hausnummer",String.valueOf(hausnummer.getText()),ContentType.create("text/plain", MIME.UTF8_CHARSET));
                entity.addTextBody("plz",String.valueOf(postleitzahl.getText()),ContentType.create("text/plain", MIME.UTF8_CHARSET));
                entity.addTextBody("stadt",newStadt,ContentType.create("text/plain", MIME.UTF8_CHARSET));
                entity.addTextBody("strasse",newStraße,ContentType.create("text/plain", MIME.UTF8_CHARSET));

                if(profil != null){
                    entity.addPart("profilbild",new FileBody(profil) );
                }
                HttpEntity requestEntity = entity.build();
                post.setEntity(requestEntity);

                try (CloseableHttpResponse response = client1.execute(post)) {
                    HttpEntity responseEntity = response.getEntity();
                    String result = EntityUtils.toString(responseEntity);
                    //Weiterleitung zur Nutzerprofil Seite
                    if (result.equals("OK")) {
                        ((Lehrender) Nutzer).setLehrstuhl(lehrstuhl.getText());
                        ((Lehrender) Nutzer).setForschungsgebiet(forschungsgebiet.getText());
                        ((Lehrender) Nutzer).getNutzerId().setPasswort(passwort.getText());
                        ((Lehrender) Nutzer).getNutzerId().setHausnummer(Integer.valueOf(hausnummer.getText()));
                        ((Lehrender) Nutzer).getNutzerId().setPlz(Integer.valueOf(postleitzahl.getText()));
                        ((Lehrender) Nutzer).getNutzerId().setStadt(stadt.getText());
                        ((Lehrender) Nutzer).getNutzerId().setStrasse(straße.getText());
                        if(profil != null) {
                            ((Lehrender) Nutzer).getNutzerId().setProfilbild(FileUtils.readFileToByteArray(profil));
                        }
                        Stage stage = (Stage) aktualisieren.getScene().getWindow();
                        Layout userprofil = null;
                        userprofil = new Layout("userprofile.fxml", stage, Nutzer);

                        if (userprofil.getController() instanceof UserprofilController) {
                            ((UserprofilController) userprofil.getController()).nutzerprofilAufrufen(Nutzer, Nutzer);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(Nutzer instanceof Student){
            String newFach = fach.getText().trim();
            newFach = newFach.replaceAll(" ", "%20");
            String newStadt = stadt.getText().trim();
            newStadt = newStadt.replaceAll(" ", "%20");
            String newStraße = straße.getText().trim();
            newStraße = newStraße.replaceAll(" ", "%20");
            try (CloseableHttpClient client1 = HttpClients.createDefault()) {

                String url = "http://localhost:8080/register/update/student";
                HttpPost post = new HttpPost(url);
                MultipartEntityBuilder entity = MultipartEntityBuilder.create();
                entity.setCharset(StandardCharsets.UTF_8);
                entity.addTextBody("nutzerId", String.valueOf(((Student) Nutzer).getNutzer().getId()));
                entity.addTextBody("passwort",passwort.getText(),ContentType.create("text/plain", MIME.UTF8_CHARSET));
                entity.addTextBody("studienfach",newFach,ContentType.create("text/plain", MIME.UTF8_CHARSET));
                entity.addTextBody("hausnummer",String.valueOf(hausnummer.getText()),ContentType.create("text/plain", MIME.UTF8_CHARSET));
                entity.addTextBody("plz",String.valueOf(postleitzahl.getText()),ContentType.create("text/plain", MIME.UTF8_CHARSET));
                entity.addTextBody("stadt",newStadt,ContentType.create("text/plain", MIME.UTF8_CHARSET));
                entity.addTextBody("strasse",newStraße,ContentType.create("text/plain", MIME.UTF8_CHARSET));

                if(profil != null){
                    entity.addPart("profilbild",new FileBody(profil) );
                }
                HttpEntity requestEntity = entity.build();
                post.setEntity(requestEntity);

                try (CloseableHttpResponse response1 = client1.execute(post)) {
                    HttpEntity responseEntity = response1.getEntity();
                    String result = EntityUtils.toString(responseEntity);
                    if (result.equals("OK")) {
                        ((Student) Nutzer).setStudienfach(fach.getText());
                        ((Student) Nutzer).getNutzer().setPasswort(passwort.getText());
                        ((Student) Nutzer).getNutzer().setHausnummer(Integer.valueOf(hausnummer.getText()));
                        ((Student) Nutzer).getNutzer().setPlz(Integer.valueOf(postleitzahl.getText()));
                        ((Student) Nutzer).getNutzer().setStadt(stadt.getText());
                        ((Student) Nutzer).getNutzer().setStrasse(straße.getText());
                        if(profil != null) {
                            ((Student) Nutzer).getNutzer().setProfilbild(FileUtils.readFileToByteArray(profil));
                        }
                        //Weiterleitung zur Nutzerprofil Seite
                        Stage stage = (Stage) aktualisieren.getScene().getWindow();
                        Layout userprofil = null;
                        userprofil = new Layout("userprofile.fxml",stage,Nutzer);

                        if(userprofil.getController() instanceof UserprofilController){
                            ((UserprofilController) userprofil.getController()).nutzerprofilAufrufen(Nutzer,Nutzer);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void Abbrechen(ActionEvent actionEvent) {
        //Weiterleitung zur Nutzerprofil Seite
        Stage stage = (Stage) abbrechen.getScene().getWindow();
        Layout userprofil = null;
            userprofil = new Layout("userprofile.fxml",stage,Nutzer);

            if(userprofil.getController() instanceof UserprofilController){
                ((UserprofilController) userprofil.getController()).nutzerprofilAufrufen(Nutzer,Nutzer);
            }
    }
}