package Client.Controller;

import Client.Layouts.Layout;
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

public class LoginController {
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
        Stage stage = (Stage) register.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("Registrieren_Student.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
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
            Stage stage = (Stage) register.getScene().getWindow();
            try {
                JSONObject jsonObject = new JSONObject(Serverantwort);
                if(jsonObject.has("matrikelnummer")){
                    System.out.println(jsonObject);
                    Student student = new Student();
                    student.addDataFromJson(jsonObject);
                    //Change View
                    Layout homeScreen = new Layout("homescreen.fxml",stage);
                    if(homeScreen.getController() instanceof HomescreenController){
                        ((HomescreenController) homeScreen.getController()).setNutzerInstanz(student);
                    }
                }else if(jsonObject.has("lehrstuhl")){
                    Lehrender lehrender = new Lehrender();
                    lehrender.addDataFromJson(jsonObject);
                    Layout homeScreen = new Layout("homescreen.fxml",stage);
                    if(homeScreen.getController() instanceof HomescreenController){
                        ((HomescreenController) homeScreen.getController()).setNutzerInstanz(lehrender);
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
