package Client.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

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

    // called by the FXML loader after the labels declared above are injected:
    public void initialize() {
        if(login.isPressed()){
            System.out.println(matrikelnummer.getText());
        }
    }
    @FXML
    private void login(ActionEvent event) {
        event.consume();
        String matr = matrikelnummer.getText();
        String pass = password.getText();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/login?matr="+matr+"&password="+pass)).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String antwort = response.body().trim();
            if(antwort.equalsIgnoreCase("true")){
                System.out.println("Eingeloggt!");
            }else{
                System.out.println("Sie konnten nicht eingeloggt werden!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
