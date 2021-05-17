package Client.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class EditierenController {
    @FXML
    private String nachname;
    @FXML
    private String vorname;
    @FXML
    private String email;
    @FXML
    private String stadt;
    @FXML
    private String straße;
    @FXML
    private String hausnummer;
    @FXML
    private String postleitzahl;
    @FXML
    private TextField lehrstuhl;
    @FXML
    private TextField forschung;
    @FXML
    private TextField fach;
    @FXML
    private Button Aktualisieren;
    @FXML
    private Button Abbrechen;

    public void create () {

    }

    public void Nutzerprofil_verändern(ActionEvent actionEvent) {
        actionEvent.consume();


        this.vorname = vorname;
        this.nachname = nachname;
        this.email = email;
        this.stadt = stadt;
        this.straße = straße;
        this.hausnummer = hausnummer;
        this.postleitzahl = postleitzahl;
        this.lehrstuhl =lehrstuhl;
        this.forschung = forschung;
        this.fach = fach;






           /* Stage stage = (Stage) Aktualisieren.getScene().getWindow();
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getClassLoader().getResource("Editieren.fxml"));
                AnchorPane root = (AnchorPane) loader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setMaximized(false);
                stage.show();
            }catch (IOException e){
                e.printStackTrace();
            } }
    }*/





        if( Aktualisieren==null){ //Nutzerprofil aktualisieren




                HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/register/student/"+
                            vorname+"&"+nachname+"&"+email+"&"+
                            hausnummer+"&"+postleitzahl+"&"+stadt+"&"+straße+"&"+fach+"&"+forschung+"&"
                    +lehrstuhl)).build();
            HttpResponse<String> response = null;
        try {

                response = client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String Serverantwort = response.body();
            System.out.println(Serverantwort);
            if(Serverantwort.equals("OK")) {
                try {
                    Stage stage = (Stage) Aktualisieren.getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getClassLoader().getResource("login.fxml"));
                    AnchorPane root = (AnchorPane) loader.load();
                    Scene scene = new Scene(root);
                    String logincss = getClass().getClassLoader().getResource("css/login.css").toExternalForm();
                    scene.getStylesheets().add(logincss);
                    stage.setScene(scene);
                    stage.setMaximized(false);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();

                System.out.println(Abbrechen);
            }
                    try {
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getClassLoader().getResource("Login.fxml"));
                        AnchorPane root = (AnchorPane) loader.load();
                        Scene scene = new Scene(root);
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.setMaximized(false);
                        stage.show();
                    }catch (IOException e){
                        e.printStackTrace();
                    }  }
                }
            }
                //Weiterleitung zur Nutzerprofil Seite




    private Node Nutzerprofil_verändern() {
    return Aktualisieren;}
}
















   /*
    public void Abbrechen (ActionEvent)  {
        actionEvent.consume();
        Stage stage = (Stage) Abbrechen.getScene().getWindow();

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("Login.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(false);
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }  }
*/
  /* public void Aktualisieren(ActionEvent actionEvent){
       actionEvent.consume();



       Stage stage = (Stage) Aktualisieren.getScene().getWindow();
       try {
           FXMLLoader loader = new FXMLLoader();
           loader.setLocation(getClass().getClassLoader().getResource("Editieren.fxml"));
           AnchorPane root = (AnchorPane) loader.load();
           Scene scene = new Scene(root);
           stage.setScene(scene);
           stage.setMaximized(false);
           stage.show();
       }catch (IOException e){
           e.printStackTrace();
       } }
}
*/












