package Client.Controller;

import Client.Layouts.Auth;
import Client.Layouts.Layout;
import Client.Modell.Lehrender;
import Client.Modell.Lehrveranstaltung;
import Client.Modell.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AuthenticationController {
    @FXML
    private TextField authfield;
    @FXML
    private Button submitbutton;
    @FXML
    private Button cancelbutton;

    private Object nutzerInstanz;


    String code=null;
    String user_code= null;

    public void sendMail(){

        HttpClient client = HttpClient.newHttpClient();
        long id=0;
        if (nutzerInstanz instanceof Lehrender) {
            id = ((Lehrender) nutzerInstanz).getNutzerId().getId();
        }
        if (nutzerInstanz instanceof Student) {
            id = ((Student) nutzerInstanz).getNutzer().getId();
        }

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/auth/"+id)).build();
        HttpResponse<String> response;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("ID "+id);
            System.out.println("RESPONSE ABFRAGE         "+response.body());
            code= response.body();





        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void submit(ActionEvent event){

        Stage stage = (Stage) submitbutton.getScene().getWindow();
        user_code=authfield.getText();
        if(user_code.equals(code)){
            Layout homeScreen = new Layout("homescreen.fxml", stage, nutzerInstanz);


            if (homeScreen.getController() instanceof HomescreenController) {
                ((HomescreenController) homeScreen.getController()).setNutzerInstanz(nutzerInstanz);
            }
        }
        else{
            System.out.println("Code falsch");
            System.out.println("Usercode " +user_code);
            System.out.println("Code "+code);
        }


    }
    public void cancelPressedButton(ActionEvent actionEvent) {
        actionEvent.consume();
        Auth login = new Auth("login.fxml", (Stage) cancelbutton.getScene().getWindow());
    }




    public void setNutzerInstanz(Object nutzerInstanz) {
        this.nutzerInstanz = nutzerInstanz;


        sendMail();
    }
}
