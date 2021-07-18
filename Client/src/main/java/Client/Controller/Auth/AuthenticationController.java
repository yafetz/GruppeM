package Client.Controller.Auth;

import Client.Controller.HomescreenController;
import Client.Layouts.Auth;
import Client.Layouts.Layout;
import Client.Modell.Lehrender;
import Client.Modell.Lehrveranstaltung;
import Client.Modell.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
    private Layout layout;

    private String code = null;
    private String user_code = null;

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
            code= response.body();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void submit(ActionEvent event){
        user_code=authfield.getText();
        if(user_code.equals(code)){
            layout.instanceLayout("homescreen.fxml");
            ((HomescreenController) layout.getController()).setLayout(layout);
            ((HomescreenController) layout.getController()).setNutzerInstanz(nutzerInstanz);
        }
        else{
            Alert fehler = new Alert(Alert.AlertType.ERROR);
            fehler.setTitle("Falscher Code!");
            fehler.setContentText("Sie haben einen falschen Code eingegeben! Sie finden ihren Code in ihrer Email!");
            fehler.setHeaderText("Falscher Code!");
            fehler.showAndWait();
//            System.out.println("Usercode " +user_code);
//            System.out.println("Code "+code);
        }
    }

    public void cancelPressedButton(ActionEvent actionEvent) {
        actionEvent.consume();
        layout.instanceAuth("login.fxml");
        ((LoginController) layout.getController()).setLayout(layout);
    }

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public void setNutzerInstanz(Object nutzerInstanz) {
        this.nutzerInstanz = nutzerInstanz;
        sendMail();
    }
}
