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
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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

    public void create () {

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
            lehrstuhl.setText(l.getLehrstuhl());
            forschungsgebiet.setText(l.getForschungsgebiet());
        }else if(nutzer instanceof Student){
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
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/register/lehrender/update/" +
                            ((Lehrender) Nutzer).getNutzerId().getId() + "&"+ passwort.getText() + "&"+lehrstuhl.getText()
                            + "&" + forschungsgebiet.getText() + "&" + hausnummer.getText() + "&"
                            + postleitzahl.getText() + "&" + stadt.getText() + "&" + straße.getText())).build();
            HttpResponse<String> response = null;
            try {
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
                String Serverantwort = response.body();
                if (Serverantwort.equals("OK")) {
                    //Weiterleitung zur Nutzerprofil Seite
                    ((Lehrender) Nutzer).setLehrstuhl(lehrstuhl.getText());
                    ((Lehrender) Nutzer).setForschungsgebiet(forschungsgebiet.getText());
                    ((Lehrender) Nutzer).getNutzerId().setPasswort(passwort.getText());
                    ((Lehrender) Nutzer).getNutzerId().setHausnummer(Integer.valueOf(hausnummer.getText()));
                    ((Lehrender) Nutzer).getNutzerId().setPlz(Integer.valueOf(postleitzahl.getText()));
                    ((Lehrender) Nutzer).getNutzerId().setStadt(stadt.getText());
                    ((Lehrender) Nutzer).getNutzerId().setStrasse(straße.getText());
                    Stage stage = (Stage) aktualisieren.getScene().getWindow();
                    Layout userprofil = null;
                    userprofil = new Layout("userprofile.fxml",stage,Nutzer);

                    if(userprofil.getController() instanceof UserprofilController){
                        ((UserprofilController) userprofil.getController()).nutzerprofilAufrufen(Nutzer,Nutzer);
                    }
                }
            }catch (IOException ie){
                ie.printStackTrace();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }else if(Nutzer instanceof Student){
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/register/student/update/" +
                            ((Student) Nutzer).getNutzer().getId() + "&"+ passwort.getText() + "&"
                            + fach.getText() + "&" + hausnummer.getText() + "&"
                            + postleitzahl.getText() + "&" + stadt.getText() + "&" + straße.getText())).build();
            HttpResponse<String> response = null;
            try {
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
                String Serverantwort = response.body();
                if (Serverantwort.equals("OK")) {
                    ((Student) Nutzer).setStudienfach(fach.getText());
                    ((Student) Nutzer).getNutzer().setPasswort(passwort.getText());
                    ((Student) Nutzer).getNutzer().setHausnummer(Integer.valueOf(hausnummer.getText()));
                    ((Student) Nutzer).getNutzer().setPlz(Integer.valueOf(postleitzahl.getText()));
                    ((Student) Nutzer).getNutzer().setStadt(stadt.getText());
                    ((Student) Nutzer).getNutzer().setStrasse(straße.getText());
                    //Weiterleitung zur Nutzerprofil Seite
                    Stage stage = (Stage) aktualisieren.getScene().getWindow();
                    Layout userprofil = null;
                    userprofil = new Layout("userprofile.fxml",stage,Nutzer);

                    if(userprofil.getController() instanceof UserprofilController){
                        ((UserprofilController) userprofil.getController()).nutzerprofilAufrufen(Nutzer,Nutzer);
                    }
                }
            }catch (IOException ie){
                ie.printStackTrace();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }

        //Weiterleitung zur Nutzerprofil Seite
        Stage stage = (Stage) aktualisieren.getScene().getWindow();
        Layout userprofil = null;
        userprofil = new Layout("userprofile.fxml",stage,Nutzer);

        if(userprofil.getController() instanceof UserprofilController){
            ((UserprofilController) userprofil.getController()).nutzerprofilAufrufen(Nutzer,Nutzer);
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












