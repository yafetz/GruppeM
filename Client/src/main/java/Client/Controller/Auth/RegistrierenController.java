package Client.Controller.Auth;
import Client.Layouts.Layout;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import org.apache.commons.io.IOUtils;
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

public class RegistrierenController {
    @FXML
    private PasswordField passwortcheck;
    @FXML
    private TextField emailcheck;
    @FXML
    private TextField email;
    @FXML
    private Button registrieren_lehrender;
    @FXML
    private Button registrieren_student;
    @FXML
    private Button zuruek;
    @FXML
    private Button registrieren;
    @FXML
    private TextField nachname;
    @FXML
    private TextField vorname;
    @FXML
    private TextField postleitzahl;
    @FXML
    private TextField stadt;
    @FXML
    private TextField strasse;
    @FXML
    private TextField hausnummer;
    @FXML
    private PasswordField passwort;
    @FXML
    private TextField studienfach;
    @FXML
    private TextField lehrstuhl;
    @FXML
    private TextField forschungsgebiet;
    @FXML
    private CheckBox check_box;
    @FXML
    private ImageView profilbild;

    private File profil;

    private Layout layout;

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public void initialize() throws URISyntaxException {
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


    public void Rollenwechsel(ActionEvent actionEvent) {
        actionEvent.consume();
        layout.instanceAuth("Registrieren_Lehrender.fxml");
        ((RegistrierenController) layout.getController()).setLayout(layout);
    }

    public void Registrieren_Student(ActionEvent actionEvent) {
        actionEvent.consume();
        layout.instanceAuth("Registrieren_Student.fxml");
        ((RegistrierenController) layout.getController()).setLayout(layout);
    }

    public void Zuruek(ActionEvent actionEvent) {
        actionEvent.consume();
        layout.instanceAuth("login.fxml");
        ((LoginController) layout.getController()).setLayout(layout);
    }

    public void Registrieren(ActionEvent actionEvent) {
        actionEvent.consume();
        if(validiert()){
            registrieren();
        }
    }

    public boolean validiert(){
        String emailText = email.getText();
        String passwortText = passwort.getText();
        String newEmail = emailText.trim();
        newEmail = newEmail.replaceAll(" ", "%20");
        String newPasswort = passwortText.trim();
        newPasswort = newPasswort.replaceAll(" ", "");

        String emailTextCheck = emailcheck.getText();
        String passwortTextCheck = passwortcheck.getText();

        String fehlermeldung = "";
        if(!newEmail.contains("@") || !newEmail.contains(".")){
            fehlermeldung += "Email ungültig bitte in Form von: name@gmail.de \n";
        }
        if(newPasswort.toCharArray().length < 8){
            fehlermeldung += "Passwort muss mindestens 8 Zeichen lang sein \n";
        }
        if(vorname.getText().equals("") || nachname.getText().equals("") || email.getText().equals("") ||
           passwortText.equals("") || stadt.getText().equals("") || strasse.getText().equals("") || postleitzahl.getText().equals("") ||
           hausnummer.getText().equals("")){
            fehlermeldung += "Es darf nichts leer sein!";
        }
        if ( !emailText.equals(emailTextCheck)) {
            fehlermeldung += "E-Mail-Angaben stimmen nicht überein! \n";
        }
        if ( !passwortText.equals(passwortTextCheck)) {
            fehlermeldung += "Passwortangaben stimmen nicht überein! \n";
        }
        if(!fehlermeldung.equals("")){
            Alert fehler = new Alert(Alert.AlertType.ERROR);
            fehler.setTitle("Eingegebene Daten sind falsch");
            fehler.setContentText(fehlermeldung);
            fehler.showAndWait();
            return false;
        }else {
            return true;
        }
    }

    public void registrieren(){
        String vornameText = vorname.getText();
        String nachnameText = nachname.getText();
        String emailText = email.getText();
        String passwortText = passwort.getText();
        String stadtText = stadt.getText();
        String strasseText = strasse.getText();

        String newVorname = vornameText.trim();
        newVorname = newVorname.replaceAll(" ", "%20");
        String newNachname = nachnameText.trim();
        newNachname = newNachname.replaceAll(" ", "%20");
        String newEmail = emailText.trim();
        newEmail = newEmail.replaceAll(" ", "%20");
        String newPasswort = passwortText.trim();
        newPasswort = newPasswort.replaceAll(" ", "");
        String newStadt = stadtText.trim();
        newStadt = newStadt.replaceAll(" ", "%20");
        String newStrasse = strasseText.trim();
        newStrasse = newStrasse.replaceAll(" ", "%20");
        if (registrieren_student == null) {
            // als Student registrieren
            String studienfachText = studienfach.getText();

            String newStudienfach = studienfachText.trim();
            newStudienfach = newStudienfach.replaceAll(" ", "%20");
            try (CloseableHttpClient client = HttpClients.createDefault()) {

                String url = "http://localhost:8080/register/student";
                HttpPost post = new HttpPost(url);
                MultipartEntityBuilder entity = MultipartEntityBuilder.create();
                entity.setCharset(StandardCharsets.UTF_8);
                entity.addTextBody("vorname",vornameText, ContentType.create("text/plain", MIME.UTF8_CHARSET));
                entity.addTextBody("nachname",nachnameText,ContentType.create("text/plain", MIME.UTF8_CHARSET));
                entity.addTextBody("email",emailText,ContentType.create("text/plain", MIME.UTF8_CHARSET));
                entity.addTextBody("passwort",passwortText,ContentType.create("text/plain", MIME.UTF8_CHARSET));
                entity.addTextBody("studienfach",studienfachText,ContentType.create("text/plain", MIME.UTF8_CHARSET));
                entity.addTextBody("hausnummer",String.valueOf(hausnummer.getText()),ContentType.create("text/plain", MIME.UTF8_CHARSET));
                entity.addTextBody("plz",String.valueOf(postleitzahl.getText()),ContentType.create("text/plain", MIME.UTF8_CHARSET));
                entity.addTextBody("stadt",stadtText,ContentType.create("text/plain", MIME.UTF8_CHARSET));
                entity.addTextBody("strasse",strasseText,ContentType.create("text/plain", MIME.UTF8_CHARSET));
                entity.addTextBody("rolle","Student");

                if(profil != null){
                    entity.addPart("profilbild",new FileBody(profil) );
                }else{
                    InputStream in = getClass().getClassLoader().getResourceAsStream("images/standardPb.png");
                    String home = System.getProperty("user.home");
                    File file = new File(home+"/Downloads/profilbild.png");
                    FileOutputStream fo = new FileOutputStream(file);
                    IOUtils.write(in.readAllBytes(),fo);
                    entity.addPart("profilbild",new FileBody(file));
                }
                HttpEntity requestEntity = entity.build();
                post.setEntity(requestEntity);

                try (CloseableHttpResponse response = client.execute(post)) {
                    HttpEntity responseEntity = response.getEntity();
                    String result = EntityUtils.toString(responseEntity);
                    layout.instanceAuth("login.fxml");
                    ((LoginController) layout.getController()).setLayout(layout);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            // als Lehrender registrieren
            String forschungsgebietText = forschungsgebiet.getText();
            String lehrstuhlText = lehrstuhl.getText();
            try (CloseableHttpClient client = HttpClients.createDefault()) {

                String url = "http://localhost:8080/register/lehrender";
                HttpPost post = new HttpPost(url);
                MultipartEntityBuilder entity = MultipartEntityBuilder.create();
                entity.setCharset(StandardCharsets.UTF_8);
                entity.addTextBody("vorname",vornameText,ContentType.create("text/plain", MIME.UTF8_CHARSET));
                entity.addTextBody("nachname",nachnameText,ContentType.create("text/plain", MIME.UTF8_CHARSET));
                entity.addTextBody("email",emailText,ContentType.create("text/plain", MIME.UTF8_CHARSET));
                entity.addTextBody("passwort",passwortText,ContentType.create("text/plain", MIME.UTF8_CHARSET));
                entity.addTextBody("lehrstuhl",lehrstuhlText,ContentType.create("text/plain", MIME.UTF8_CHARSET));
                entity.addTextBody("forschungsgebiet",forschungsgebietText,ContentType.create("text/plain", MIME.UTF8_CHARSET));
                entity.addTextBody("hausnummer",String.valueOf(hausnummer.getText()),ContentType.create("text/plain", MIME.UTF8_CHARSET));
                entity.addTextBody("plz",String.valueOf(postleitzahl.getText()),ContentType.create("text/plain", MIME.UTF8_CHARSET));
                entity.addTextBody("stadt",stadtText,ContentType.create("text/plain", MIME.UTF8_CHARSET));
                entity.addTextBody("strasse",strasseText,ContentType.create("text/plain", MIME.UTF8_CHARSET));
                entity.addTextBody("rolle","Lehrender");

                if(profil != null){
                    entity.addPart("profilbild",new FileBody(profil) );
                }else{
                    InputStream in = getClass().getClassLoader().getResourceAsStream("images/standardPb.png");
                    String home = System.getProperty("user.home");
                    File file = new File(home+"/Downloads/profilbild.png");
                    FileOutputStream fo = new FileOutputStream(file);
                    IOUtils.write(in.readAllBytes(),fo);
                    entity.addPart("profilbild",new FileBody(file));
                }
                HttpEntity requestEntity = entity.build();
                post.setEntity(requestEntity);

                try (CloseableHttpResponse response = client.execute(post)) {
                    HttpEntity responseEntity = response.getEntity();
                    String result = EntityUtils.toString(responseEntity);
                    layout.instanceAuth("login.fxml");
                    ((LoginController) layout.getController()).setLayout(layout);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}