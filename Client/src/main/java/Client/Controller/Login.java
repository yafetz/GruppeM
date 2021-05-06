package Client.Controller;

import Client.Modell.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Login {
    @FXML
    private TextField matrikelnummer ;
    @FXML
    private TextField password ;
    @FXML
    private Button login;
    @FXML
    private Button register;
    // called by the FXML loader after the labels declared above are injected:
    public void initialize() {

    }

    @FXML
    private void registerPressedButton(ActionEvent event) {

    }

    @FXML
    private void loginPressedButton(ActionEvent event) {
        event.consume();
        String matr = matrikelnummer.getText();
        String pass = password.getText();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/login/"+matr+"&"+pass)).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String Serverantwort = response.body();
            try {
                JSONObject jsonObject = new JSONObject(Serverantwort);
                if(jsonObject.has("matrikelnummer")){
                    Student student = new Student();
                    student.setId(jsonObject.getInt("id"));
                    student.setMatrikelnummer(jsonObject.getInt("matrikelnummer"));
                    student.setStudienfach(jsonObject.getString("studienfach"));
                    Nutzer nutzer = new Nutzer();
                    JSONObject jsonNutzer = (JSONObject) jsonObject.get("nutzerId");
                    nutzer.setId(jsonNutzer.getInt("id"));
                    nutzer.setVorname(jsonNutzer.getString("vorname"));
                    nutzer.setNachname(jsonNutzer.getString("nachname"));
                    nutzer.setEmail(jsonNutzer.getString("email"));
                    nutzer.setPasswort(jsonNutzer.getString("passwort"));
                    nutzer.setProfilbild("null");
                    nutzer.setStrasse(jsonNutzer.getString("strasse"));
                    nutzer.setHausnummer(jsonNutzer.getInt("hausnummer"));
                    nutzer.setPlz(jsonNutzer.getInt("plz"));
                    student.setNutzer(nutzer);
                    Stage stage = (Stage) register.getScene().getWindow();
                    try {
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getClassLoader().getResource("userprofile.fxml"));
                        AnchorPane root = (AnchorPane) loader.load();
                        Userprofil userprofil = loader.getController();
                        userprofil.setStudent(student);
                        Scene scene = new Scene(root);
                        String homescreencss = getClass().getClassLoader().getResource("css/login.css").toExternalForm();
                        scene.getStylesheets().add(homescreencss);
                        stage.setScene(scene);
                        stage.setMaximized(true);
                        stage.show();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }catch (JSONException err){
                err.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
