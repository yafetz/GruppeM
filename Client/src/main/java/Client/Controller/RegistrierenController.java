package Client.Controller;
import Client.Layouts.Auth;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
import java.net.URISyntaxException;

public class RegistrierenController {
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
        Stage stage = (Stage) registrieren_lehrender.getScene().getWindow();
        Auth register_lehrender = new Auth("Registrieren_Lehrender.fxml",stage);
    }

    public void Registrieren_Student(ActionEvent actionEvent) {
        actionEvent.consume();
        Stage stage = (Stage) registrieren_student.getScene().getWindow();
        Auth register_student = new Auth("Registrieren_Student.fxml",stage);
    }

    public void Zuruek(ActionEvent actionEvent) {
        actionEvent.consume();
        Stage stage = (Stage) zuruek.getScene().getWindow();
        Auth login = new Auth("login.fxml",stage);
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

                entity.addTextBody("vorname",vornameText);
                entity.addTextBody("nachname",nachnameText);
                entity.addTextBody("email",emailText);
                entity.addTextBody("passwort",passwortText);
                entity.addTextBody("studienfach",studienfachText);
                entity.addTextBody("hausnummer",String.valueOf(hausnummer.getText()));
                entity.addTextBody("plz",String.valueOf(postleitzahl.getText()));
                entity.addTextBody("stadt",stadtText);
                entity.addTextBody("strasse",strasseText);
                entity.addTextBody("rolle","Student");

                if(profil != null){
                    entity.addPart("profilbild",new FileBody(profil) );
                }else{
                    File standard = new File(getClass().getClassLoader().getResource("images/standardPb.png").toURI());
                    entity.addPart("profilbild",new FileBody(standard));
                }

                HttpEntity requestEntity = entity.build();
                post.setEntity(requestEntity);

                try (CloseableHttpResponse response = client.execute(post)) {
                    HttpEntity responseEntity = response.getEntity();
                    String result = EntityUtils.toString(responseEntity);
                    System.out.println(result);
                    Auth login = new Auth("login.fxml",(Stage) registrieren.getScene().getWindow());
                }
            } catch (IOException | URISyntaxException e) {
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

                entity.addTextBody("vorname",vornameText);
                entity.addTextBody("nachname",nachnameText);
                entity.addTextBody("email",emailText);
                entity.addTextBody("passwort",passwortText);
                entity.addTextBody("lehrstuhl",lehrstuhlText);
                entity.addTextBody("forschungsgebiet",forschungsgebietText);
                entity.addTextBody("hausnummer",String.valueOf(hausnummer.getText()));
                entity.addTextBody("plz",String.valueOf(postleitzahl.getText()));
                entity.addTextBody("stadt",stadtText);
                entity.addTextBody("strasse",strasseText);
                entity.addTextBody("rolle","Lehrender");

                if(profil != null){
                    entity.addPart("profilbild",new FileBody(profil) );
                }else{
                    File standard = new File(getClass().getClassLoader().getResource("images/standardPb.png").toURI());
                    entity.addPart("profilbild",new FileBody(standard));
                }

                HttpEntity requestEntity = entity.build();
                post.setEntity(requestEntity);

                try (CloseableHttpResponse response = client.execute(post)) {
                    HttpEntity responseEntity = response.getEntity();
                    String result = EntityUtils.toString(responseEntity);
                    System.out.println(result);
                    Auth login = new Auth("login.fxml",(Stage) registrieren.getScene().getWindow());
                }
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }
}