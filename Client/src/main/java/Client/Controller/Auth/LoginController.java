package Client.Controller.Auth;

import Client.Controller.HomescreenController;
import Client.Controller.SettingController;
import Client.Layouts.Layout;
import Client.Modell.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class LoginController {

    @FXML
    private TextField matrikelnummer ;
    @FXML
    private TextField password ;
    @FXML
    private Button login;
    @FXML
    private Button register;
    @FXML
    private Button setting;

    private Layout layout;
    //If 0 then deactivate 2 Faktor, if 1 activate
    private int auth = 1;
    private LocalDateTime jetzt;

    public LocalDateTime getJetzt() {
        return jetzt;
    }

    public void setJetzt(LocalDateTime jetzt) {
        this.jetzt = jetzt;
    }

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
        if(jetzt==null) {
            jetzt=LocalDateTime.now();
            setTimeAndDate(jetzt);
        }
        else {
            setTimeAndDate(jetzt);
        }

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




        public void setTimeAndDate(LocalDateTime aktuell) {



        try (CloseableHttpClient client = HttpClients.createDefault()) {
        String url = "http://localhost:8080/login/uploadDateAndTime";
        HttpPost post = new HttpPost(url);
        MultipartEntityBuilder entity = MultipartEntityBuilder.create();

        entity.addTextBody("datum", String.valueOf(aktuell));

        HttpEntity requestEntity = entity.build();
        post.setEntity(requestEntity);
        try (CloseableHttpResponse response = client.execute(post)) {
            HttpEntity responseEntity = response.getEntity();
            String result = EntityUtils.toString(responseEntity);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }




    }

    public void settingPressedButton(ActionEvent actionEvent) {
        actionEvent.consume();
        layout.instanceAuth("settings.fxml");
        ((SettingController) layout.getController()).setLayout(layout);
    }
}
