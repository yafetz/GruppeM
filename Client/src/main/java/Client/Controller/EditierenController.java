package Client.Controller;

import Client.Layouts.Layout;
import Client.Modell.Lehrender;
import Client.Modell.Student;
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
    private TextField nachname;
    @FXML
    private TextField vorname;
    @FXML
    private TextField email;
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
    private Button aktualisieren;
    @FXML
    private Button abbrechen;

    private Object Nutzer;
    public void create () {

    }

    public Object getNutzer() {
        return Nutzer;
    }

    public void setNutzer(Object nutzer) {
        Nutzer = nutzer;
        if(nutzer instanceof Lehrender){
            Lehrender l = (Lehrender) nutzer;
            vorname.setText(l.getNutzerId().getVorname());
            nachname.setText(l.getNutzerId().getNachname());
            email.setText(l.getNutzerId().getEmail());
            stadt.setText(l.getNutzerId().getStadt());
            straße.setText(l.getNutzerId().getStrasse());
            hausnummer.setText(String.valueOf(l.getNutzerId().getHausnummer()));
            postleitzahl.setText(String.valueOf(l.getNutzerId().getPlz()));
            fach.setVisible(false);
            lehrstuhl.setText(l.getLehrstuhl());
            forschungsgebiet.setText(l.getForschungsgebiet());
        }else if(nutzer instanceof Student){
            Student s = (Student) nutzer;
            vorname.setText(s.getNutzer().getVorname());
            nachname.setText(s.getNutzer().getNachname());
            email.setText(s.getNutzer().getEmail());
            stadt.setText(s.getNutzer().getStadt());
            straße.setText(s.getNutzer().getStrasse());
            hausnummer.setText(String.valueOf(s.getNutzer().getHausnummer()));
            postleitzahl.setText(String.valueOf(s.getNutzer().getPlz()));
            lehrstuhl.setVisible(false);
            forschungsgebiet.setVisible(false);
            fach.setText(s.getStudienfach());
        }
    }

    public void Nutzerprofil_verändern(ActionEvent actionEvent) {
        actionEvent.consume();


        if( aktualisieren==null){ //Nutzerprofil aktualisieren

                HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/register/student/"+
                            vorname+"&"+nachname+"&"+email+"&"+
                            hausnummer+"&"+postleitzahl+"&"+stadt+"&"+straße+"&"+fach+"&"+forschungsgebiet+"&"
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
                    Stage stage = (Stage) aktualisieren.getScene().getWindow();
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

                System.out.println(abbrechen);
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












