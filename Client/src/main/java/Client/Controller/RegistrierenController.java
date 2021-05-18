package Client.Controller;

import Client.Layouts.Auth;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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




    public void Rollenwechsel(ActionEvent actionEvent) {
        actionEvent.consume();
        Stage stage = (Stage) registrieren_lehrender.getScene().getWindow();
        Auth register_lehrender = new Auth("registerlehrender.fxml",stage);
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
        String vornameText = vorname.getText();
        String nachnameText = nachname.getText();
        String emailText = email.getText();
        String passwortText = passwort.getText();
        int hausnummerText = Integer.valueOf(hausnummer.getText());
        int plzText = Integer.valueOf(postleitzahl.getText());
        String stadtText = stadt.getText();
        String strasseText = strasse.getText();
        if( registrieren_student==null){
            // als Student registrieren
            String studienfachText = studienfach.getText();
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/register/student/"+
                            vornameText+"&"+nachnameText+"&"+emailText+"&"+
                            passwortText+"&"+studienfachText+"&"+
                            hausnummerText+"&"+plzText+"&"+stadtText+"&"+strasseText)).build();
            HttpResponse<String> response = null;
            try {
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
                String Serverantwort = response.body();
                System.out.println(Serverantwort);
                if(Serverantwort.equals("OK")){
                    //Weiterleitung zur Login Seite
                    Stage stage = (Stage) registrieren.getScene().getWindow();
                    Auth login = new Auth("login.fxml",stage);
                }
            }catch (IOException | InterruptedException e){
                e.printStackTrace();
            }

            } else {
            // als Lehrender registrieren
            String forschungsgebietText = forschungsgebiet.getText();
            String lehrstuhlText = lehrstuhl.getText();
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/register/lehrender/"+
                            vornameText+"&"+nachnameText+"&"+emailText+"&"+
                            passwortText+"&"+forschungsgebietText+"&"+lehrstuhlText+"&"+
                            hausnummerText+"&"+plzText+"&"+stadtText+"&"+strasseText)).build();
            HttpResponse<String> response = null;
            try {
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
                String Serverantwort = response.body();
                System.out.println(Serverantwort);
                if(Serverantwort.equals("OK")){
                    //Weiterleitung zur Login Seite
                    try {
                        Stage stage = (Stage) registrieren.getScene().getWindow();
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getClassLoader().getResource("login.fxml"));
                        AnchorPane root = (AnchorPane) loader.load();
                        Scene scene = new Scene(root);
                        String logincss = getClass().getClassLoader().getResource("css/login.css").toExternalForm();
                        scene.getStylesheets().add(logincss);
                        stage.setScene(scene);
                        stage.setMaximized(false);
                        stage.show();
                    }
                    catch(Exception e)    {
                        e.printStackTrace();
                    }
                }
            }catch (IOException | InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
