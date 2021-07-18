package Client.Controller.Chat;

import Client.Layouts.Layout;
import Client.Modell.Lehrender;
import Client.Modell.Student;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
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
    private boolean finished = true;

    private HttpClient client;
    private HttpRequest request;
    public boolean chatleft = true;
    public Thread t;
    private boolean running = true;
    private int currentLength = 0;

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

    public void setNutzer(Object nutzer) {
        this.nutzer = nutzer;
    }

    public void scheduler() {
        System.out.println("Main : " + Thread.currentThread().getId());
        Thread main = Thread.currentThread();
        Task<Void> t = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                boolean test = true;
                while (test) {
                    LadeNeueNachrichten();
                    Thread.sleep(1000);
                }
                return null;
            }
        };
        this.t = new Thread(t);
        this.t.start();
    }

    static JSONArray array = new JSONArray();

    public String LadeNeueNachrichten() {
        finished = false;
        System.out.println("Start : " + System.currentTimeMillis() +" chat ID: "+ chatraumid);
        if (client == null) {
            client = HttpClient.newHttpClient();
        }
        if (request == null) {
            request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/chat/alleNachrichten/" + chatraumid)).build();
        }
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //ObjectMapper mapper = new ObjectMapper();
            array = new JSONArray(response.body());
            if (currentLength != array.length()) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
//                        System.out.println("2");
                        for (int i = 0; i < array.length(); i++) {
                            //Check if this is a message which is not shown
                            if (toIntExact(array.getJSONObject(i).getLong("id")) > lastMessage) {
                                lastMessage = toIntExact(array.getJSONObject(i).getLong("id"));
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                LocalDateTime dateTime = LocalDateTime.parse(array.getJSONObject(i).getString("datum").replace("T", " "), formatter);
                                String datum = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss").format(dateTime);
                                //Check if the Message was send by the user
                                JSONObject nutzerObject = array.getJSONObject(i).getJSONObject("nutzer");
                                if (nutzer instanceof Student) {
                                    if (((Student) nutzer).getNutzer().getId() == toIntExact(nutzerObject.getLong("id"))) {
                                        //Eigener Nutzer
                                        eigeneNachrichtAnzeigen(array.getJSONObject(i).getString("nachricht"), datum);
                                    } else {
                                        andereNachrichtAnzeigen(array.getJSONObject(i).getString("nachricht"), nutzerObject.getString("vorname") + " " + nutzerObject.getString("nachname"), datum);
                                    }
                                } else if (nutzer instanceof Lehrender) {
                                    if (((Lehrender) nutzer).getNutzerId().getId() == toIntExact(nutzerObject.getLong("id"))) {
                                        //Eigener Nutzer
                                        eigeneNachrichtAnzeigen(array.getJSONObject(i).getString("nachricht"), datum);
                                    } else {
                                        andereNachrichtAnzeigen(array.getJSONObject(i).getString("nachricht"), nutzerObject.getString("vorname") + " " + nutzerObject.getString("nachname"), datum);
                                    }
                                }
                            }
                        }
                    }
                });
                currentLength = array.length();
                return "Nachrichten geladen";
            }


        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("End : " + System.currentTimeMillis());
        finished = true;
        return "Nachrichten konnten nicht geladen";
    }



    public void sendeNachricht(ActionEvent actionEvent) {
        if (nachricht.getText() != "") {
            senden.setDisable(true);
            addNachrichtToDatabase();
            nachricht.setText("");
        }
    }



    private String addNachrichtToDatabase() {
        try (CloseableHttpClient client1 = HttpClients.createDefault()) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String url = "http://localhost:8080/chat/neueNachricht";
            HttpPost post = new HttpPost(url);
            MultipartEntityBuilder entity = MultipartEntityBuilder.create();
            entity.setCharset(StandardCharsets.UTF_8);
            entity.addTextBody("chat_id", String.valueOf(chatraumid));
            entity.addTextBody("nachricht", nachricht.getText());
            entity.addTextBody("datum", dtf.format(now));
            if (nutzer instanceof Student) {
                entity.addTextBody("nutzer_id", String.valueOf(((Student) nutzer).getNutzer().getId()));
            } else if (nutzer instanceof Lehrender) {
                entity.addTextBody("nutzer_id", String.valueOf(((Lehrender) nutzer).getNutzerId().getId()));
            }
            HttpEntity requestEntity = entity.build();
            post.setEntity(requestEntity);
//            System.out.println("Sende Nachricht");
            try (CloseableHttpResponse response = client1.execute(post)) {
                HttpEntity responseEntity = response.getEntity();
                String result = EntityUtils.toString(responseEntity);
                //Weiterleitung zur Nutzerprofil Seite
                if (result.equals("OK")) {
                    senden.setDisable(false);
                    return "Senden erfolgreich";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Senden fehlgeschlagen";
    }

    private void eigeneNachrichtAnzeigen(String text, String date) {
        VBox meineNachricht = new VBox();
        meineNachricht.setAlignment(Pos.TOP_RIGHT);
        meineNachricht.setPadding(new Insets(0, 20, 0, 500));

        Label person = new Label();
        person.setText("Ich");
        person.setPadding(new Insets(2, 10, 2, 10));

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
        datum.setPadding(new Insets(2, 10, 2, 10));

        meineNachricht.getChildren().add(person);
        meineNachricht.getChildren().add(nachrichtText);
        meineNachricht.getChildren().add(datum);

        chat.getChildren().add(meineNachricht);
    }

    private void andereNachrichtAnzeigen(String text, String name, String date) {
        VBox meineNachricht = new VBox();
        meineNachricht.setAlignment(Pos.TOP_LEFT);
        meineNachricht.setPadding(new Insets(0, 500, 0, 20));

        Label person = new Label();
        person.setText(name);
        person.setPadding(new Insets(2, 10, 2, 10));

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
        datum.setPadding(new Insets(2, 10, 2, 10));

        meineNachricht.getChildren().add(person);
        meineNachricht.getChildren().add(nachrichtText);
        meineNachricht.getChildren().add(datum);

        chat.getChildren().add(meineNachricht);
    }

}
