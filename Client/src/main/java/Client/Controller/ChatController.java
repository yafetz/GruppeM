package Client.Controller;

import Client.Layouts.Layout;
import Client.Modell.Lehrender;
import Client.Modell.Projektgruppe;
import Client.Modell.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

import javafx.concurrent.*;

import static java.lang.Math.toIntExact;

public class ChatController {

    @FXML
    private Button senden;
    @FXML
    private TextField nachricht;
    @FXML
    private VBox chat;

    private int chatraumid;
    
    private Object nutzer;

    private int lastMessage = 0;
    private Layout layout;

    private Thread t;
    private boolean running = true;

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public int getChatraumid() {
        return chatraumid;
    }

    public void setChatraumid(int chatraumid) {
        this.chatraumid = chatraumid;
    }

    public void setNutzer(Object nutzer){
        this.nutzer = nutzer;
    }

    public void scheduler(){
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        LadeNeueNachrichten();
                    }
                });
            }
        }, 0, 1000);
    }

    public void LadeNeueNachrichten(){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/chat/alleNachrichten/" + chatraumid)).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //ObjectMapper mapper = new ObjectMapper();
            JSONArray array = new JSONArray(response.body());
            for(int i = 0; i < array.length(); i++){
                //Check if this is a message which is not shown
                if(toIntExact(array.getJSONObject(i).getLong("id")) > lastMessage){
                    lastMessage = toIntExact(array.getJSONObject(i).getLong("id"));
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime dateTime = LocalDateTime.parse(array.getJSONObject(i).getString("datum").replace("T"," "), formatter);
                    String datum = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss").format(dateTime);
                    //Check if the Message was send by the user
                    JSONObject nutzerObject = array.getJSONObject(i).getJSONObject("nutzer");
                    if(nutzer instanceof Student){
                        if(((Student) nutzer).getNutzer().getId() == toIntExact(nutzerObject.getLong("id"))){
                            //Eigener Nutzer
                            eigeneNachrichtAnzeigen(array.getJSONObject(i).getString("nachricht"),datum);
                        }else{
                            andereNachrichtAnzeigen(array.getJSONObject(i).getString("nachricht"),nutzerObject.getString("vorname")+" "+nutzerObject.getString("nachname"),datum);
                        }
                    }else if(nutzer instanceof Lehrender){
                        if(((Lehrender) nutzer).getNutzerId().getId() == toIntExact(nutzerObject.getLong("id"))){
                            //Eigener Nutzer
                            eigeneNachrichtAnzeigen(array.getJSONObject(i).getString("nachricht"),datum);
                        }else{
                            andereNachrichtAnzeigen(array.getJSONObject(i).getString("nachricht"),nutzerObject.getString("vorname")+" "+nutzerObject.getString("nachname"),datum);
                        }
                    }
                }
            }
            array = null;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendeNachricht(ActionEvent actionEvent) {
        if(nachricht.getText() != "") {
            senden.setDisable(true);
            addNachrichtToDatabase();
            nachricht.setText("");
        }
    }

    private void addNachrichtToDatabase(){
        try (CloseableHttpClient client1 = HttpClients.createDefault()) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String url = "http://localhost:8080/chat/neueNachricht";
            HttpPost post = new HttpPost(url);
            MultipartEntityBuilder entity = MultipartEntityBuilder.create();
            entity.setCharset(StandardCharsets.UTF_8);
            entity.addTextBody("chat_id", String.valueOf(chatraumid) );
            entity.addTextBody("nachricht", nachricht.getText());
            entity.addTextBody("datum", dtf.format(now));
            if(nutzer instanceof Student){
                entity.addTextBody("nutzer_id", String.valueOf(((Student) nutzer).getNutzer().getId()));
            }else if(nutzer instanceof Lehrender){
                entity.addTextBody("nutzer_id", String.valueOf(((Lehrender) nutzer).getNutzerId().getId()));
            }
            HttpEntity requestEntity = entity.build();
            post.setEntity(requestEntity);
            System.out.println("Sende Nachricht");
            try (CloseableHttpResponse response = client1.execute(post)) {
                HttpEntity responseEntity = response.getEntity();
                String result = EntityUtils.toString(responseEntity);
                //Weiterleitung zur Nutzerprofil Seite
                if (result.equals("OK")) {
                    senden.setDisable(false);
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        }

    private void eigeneNachrichtAnzeigen(String text,String date){
        VBox meineNachricht = new VBox();
        meineNachricht.setAlignment(Pos.TOP_RIGHT);
        meineNachricht.setPadding(new Insets(0,20,0,500));

        Label person = new Label();
        person.setText("Ich");
        person.setPadding(new Insets(2,10,2,10));

        Label nachrichtText = new Label();
        nachrichtText.setStyle("-fx-background-color: dodgerblue; -fx-background-radius: 10;");
        nachrichtText.setText(text);
        nachrichtText.setWrapText(true);
        nachrichtText.setTextOverrun(OverrunStyle.CLIP);
        //nachrichtText.setMaxWidth(500.0);
        nachrichtText.setFont(new Font(20));
        nachrichtText.setPadding(new Insets(2, 10, 2, 10));
        nachrichtText.setTextFill(Paint.valueOf("white"));

        Label datum = new Label();
        datum.setText(date);
        datum.setPadding(new Insets(2,10,2,10));

        meineNachricht.getChildren().add(person);
        meineNachricht.getChildren().add(nachrichtText);
        meineNachricht.getChildren().add(datum);

        chat.getChildren().add(meineNachricht);
    }

    private void andereNachrichtAnzeigen(String text,String name,String date){
        VBox meineNachricht = new VBox();
        meineNachricht.setAlignment(Pos.TOP_LEFT);
        meineNachricht.setPadding(new Insets(0,500,0,20));

        Label person = new Label();
        person.setText(name);
        person.setPadding(new Insets(2,10,2,10));

        Label nachrichtText = new Label();
        nachrichtText.setStyle("-fx-background-color: lightgray; -fx-background-radius: 10;");
        nachrichtText.setText(text);
        nachrichtText.setWrapText(true);
        nachrichtText.setTextOverrun(OverrunStyle.CLIP);
        //nachrichtText.setMaxWidth(500.0);
        nachrichtText.setFont(new Font(20));
        nachrichtText.setPadding(new Insets(2, 10, 2, 10));
        nachrichtText.setTextFill(Paint.valueOf("white"));

        Label datum = new Label();
        datum.setText(date);
        datum.setPadding(new Insets(2,10,2,10));

        meineNachricht.getChildren().add(person);
        meineNachricht.getChildren().add(nachrichtText);
        meineNachricht.getChildren().add(datum);

        chat.getChildren().add(meineNachricht);
    }

}
