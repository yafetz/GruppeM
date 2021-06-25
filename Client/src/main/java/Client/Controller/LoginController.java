package Client.Controller;

import Client.Layouts.Auth;
import Client.Layouts.Layout;
import Client.Modell.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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

public class LoginController {
    @FXML
    private TextField matrikelnummer ;
    @FXML
    private TextField password ;
    @FXML
    private Button login;
    @FXML
    private Button register;

    private Layout layout;
    //If 0 then deactivate 2 Faktor, if 1 activate
    private int auth = 0;

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    // called by the FXML loader after the labels declared above are injected:
    public void initialize() {
    }

    @FXML
    private void registerPressedButton(ActionEvent event) {
        layout.instanceAuth("Registrieren_Student.fxml");
        ((RegistrierenController) layout.getController()).setLayout(layout);
    }

    @FXML
    private void loginPressedButton(ActionEvent event) {
        event.consume();
        String matr = matrikelnummer.getText().trim().replaceAll(" ","%20");
        String pass = password.getText().trim().replaceAll(" ","%20");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/login/"+matr+"&"+pass)).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String Serverantwort = response.body();
            Stage stage = (Stage) register.getScene().getWindow();
            if(Serverantwort.equals("")){
                Alert fehler = new Alert(Alert.AlertType.ERROR);
                fehler.setTitle("Falsche Anmeldedaten!");
                fehler.setContentText("Ungültige Logindaten!");
                fehler.showAndWait();
            }else {
                try {
                    JSONObject jsonObject = new JSONObject(Serverantwort);
                    if (jsonObject.has("matrikelnummer")) {
                        Student student = new Student();

                        student.addDataFromJson(jsonObject);
                        //Change View
                        layout.setNutzer(student);
                        if(auth == 1) {
                            layout.instanceAuth("auth.fxml");
                            ((AuthenticationController) layout.getController()).setLayout(layout);
                            ((AuthenticationController) layout.getController()).setNutzerInstanz(student);
                        }else if(auth == 0){
                            layout.instanceLayout("homescreen.fxml");
                            ((HomescreenController) layout.getController()).setLayout(layout);
                            ((HomescreenController) layout.getController()).setNutzerInstanz(student);
                        }

                    } else if (jsonObject.has("lehrstuhl")) {
                        Lehrender lehrender = new Lehrender();
                        lehrender.addDataFromJson(jsonObject);
                        //Change View
                        layout.setNutzer(lehrender);
                        if(auth == 1) {
                            layout.instanceAuth("auth.fxml");
                            ((AuthenticationController) layout.getController()).setLayout(layout);
                            ((AuthenticationController) layout.getController()).setNutzerInstanz(lehrender);
                        }else if(auth == 0){
                            layout.instanceLayout("homescreen.fxml");
                            ((HomescreenController) layout.getController()).setLayout(layout);
                            ((HomescreenController) layout.getController()).setNutzerInstanz(lehrender);
                        }
                    }

                } catch (JSONException err) {
                    err.printStackTrace();
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
