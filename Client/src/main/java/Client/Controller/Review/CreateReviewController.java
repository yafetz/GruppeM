
package Client.Controller.Review;


import Client.Controller.Lehrveranstaltung.LehrveranstaltungsuebersichtsseiteController;
import Client.Layouts.Layout;
import Client.Modell.Lehrender;
import Client.Modell.Lehrveranstaltung;
import Client.Modell.Student;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateReviewController {
    @FXML
    public Label review_LvTitel_Label;
    @FXML
    public TextArea questionTextArea;
    @FXML
    public TextArea answerTextArea;
    @FXML
    private Button newQuestion;
    @FXML
    private Button create;
    @FXML
    private Button newAnswer;
    @FXML
    private TextField review_titel;



   // private HashMap<String, Boolean> answers;
    private ArrayList<String> answers;
    private HashMap<String, ArrayList<String>> questions;
    private Object nutzer;
    private Lehrveranstaltung lehrveranstaltung;
    private Layout layout;


    public void initialize() {
        answers= new ArrayList<>();
        questions = new HashMap<>();

    }


    public void setNutzer(Object nutzer, Lehrveranstaltung lehrveranstaltung) {
        this.nutzer=nutzer;
        this.lehrveranstaltung=lehrveranstaltung;
    }


    public void pressedNewQuestion(ActionEvent actionEvent) {
        if(answers.size() > 0) {
            if (questionTextArea.getText() != ""
                    && !questionTextArea.getText().contains(",")
                    && !questionTextArea.getText().contains(":")
                    && !questionTextArea.getText().contains("{")
                    && !questionTextArea.getText().contains("}")) {
                if (answerTextArea.getText() != "") {
                    answers.add(answerTextArea.getText());

                }
                answerTextArea.setText("");
                questions.put(questionTextArea.getText(), answers);

                questionTextArea.setText("");
                answers = new ArrayList<>();
            } else {
                Alert fehler = new Alert(Alert.AlertType.ERROR);
                fehler.setTitle("Frage Eingabe ungültig!");
                fehler.setContentText("Frage darf nicht leer sein und nicht { } , : enthalten!");
                fehler.showAndWait();
            }
        }else{
            Alert fehler = new Alert(Alert.AlertType.ERROR);
            fehler.setTitle("Keine Antworten für die Frage gespeichert!");
            fehler.setContentText("Sie müssen Antworten eingeben bevor Sie eine Neue Frage hinzufügen können! ");
            fehler.showAndWait();
        }
    }

    public void pressedCreate(ActionEvent actionEvent) {

        actionEvent.consume();
        if(questions.size() > 0){
            try (CloseableHttpClient client = HttpClients.createDefault()) {

                String url = "http://localhost:8080/review/createReview";
                HttpPost post = new HttpPost(url);
                MultipartEntityBuilder entity = MultipartEntityBuilder.create();
                entity.addTextBody("titel", review_titel.getText());
                entity.addTextBody("lehrenderId", String.valueOf(((Lehrender) nutzer).getId()));
                entity.addTextBody("lehrveranstaltungsId", String.valueOf(lehrveranstaltung.getId()));

                HttpEntity requestEntity = entity.build();
                post.setEntity(requestEntity);

                try (CloseableHttpResponse response = client.execute(post)) {
                    HttpEntity responseEntity = response.getEntity();
                    String result = EntityUtils.toString(responseEntity);
                     System.out.println("RESULT        "+result);
                    if(result.contains("OK:")) {
                        try (CloseableHttpClient client1 = HttpClients.createDefault()) {

                            String url1 = "http://localhost:8080/review/createQuestion";
                            HttpPost post1 = new HttpPost(url1);
                            MultipartEntityBuilder entity1 = MultipartEntityBuilder.create();
                            entity1.addTextBody("review", result.split(":")[1]);
                            int index = 0;
                            for(Map.Entry<String, ArrayList<String>> entry : questions.entrySet()) {
                                String question = entry.getKey();
                                ArrayList<String> answers = entry.getValue();
                                String frage = question+";";

                                for(int i=0; i<answers.size();i++){
                                    frage += answers.get(i)+";";
                                }
                                entity1.addTextBody("question", frage);
                            }

                            HttpEntity requestEntity1 = entity1.build();
                            post1.setEntity(requestEntity1);

                            try (CloseableHttpResponse response1 = client1.execute(post1)) {
                                HttpEntity responseEntity1 = response1.getEntity();
                                String result1 = EntityUtils.toString(responseEntity1);
                               System.out.println("Fragen gesendet!");
                                if(result1.contains("OK")) {

                                    layout.instanceLayout("lehrveranstaltungsuebersichtsseite.fxml");
                                    ((LehrveranstaltungsuebersichtsseiteController) layout.getController()).setLayout(layout);
                                    ((LehrveranstaltungsuebersichtsseiteController) layout.getController()).uebersichtsseiteAufrufen(nutzer, lehrveranstaltung);
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Alert fehler = new Alert(Alert.AlertType.ERROR);
            fehler.setTitle("Review kann nicht erstellt werden!");
            fehler.setContentText("Sie müssen mindestens eine Frage samt Antworten hinzugefügt haben!");
            fehler.showAndWait();
        }
    }

    public void pressedNewAnswer(ActionEvent actionEvent) {
//        System.out.println("Eingegebene Antwortmöglichkeit: " + answerTextArea.getText());
        if (answerTextArea.getText() != ""
                && !questionTextArea.getText().contains(",")
                && !questionTextArea.getText().contains(":")
                && !questionTextArea.getText().contains("{")
                && !questionTextArea.getText().contains("}")) {
            answers.add(answerTextArea.getText());
            answerTextArea.setText("");

        }else{
            Alert fehler = new Alert(Alert.AlertType.ERROR);
            fehler.setTitle("Antwort Eingabe ungültig!");
            fehler.setContentText("Antwort darf nicht leer sein und nicht { } , : enthalten!");
            fehler.showAndWait();
        }
    }

  public void reviewSeiteAufrufen(Object nutzer, Lehrveranstaltung lehrveranstaltung) {
        this.nutzer = nutzer;
        this.lehrveranstaltung = lehrveranstaltung;

        if (nutzer != null) {
            if (nutzer instanceof Lehrender) {
                review_LvTitel_Label.setText(((Lehrveranstaltung) lehrveranstaltung).getTitel());

            } else if (nutzer instanceof Student) {
                review_LvTitel_Label.setText(((Lehrveranstaltung) lehrveranstaltung).getTitel());
               // createQuizButton.setVisible(false);
            }

        }

    }



    public void setNutzer (Object nutzer){
    }

  /*  public void pressedCreateReviewButton (ActionEvent actionEvent) {
        actionEvent.consume();
        layout.instanceLayout("createQuiz.fxml");
        ((CreateQuizController) layout.getController()).setLayout(layout);
        ((CreateQuizController) layout.getController()).setNutzer(nutzer,lehrveranstaltung);
        ((CreateQuizController) layout.getController()).quiz_LvTitel_Label.setText("Lehrveranstaltung " + lehrveranstaltung.getTitel());
    }

*/
    public void setLayout(Layout layout) {
        this.layout = layout;
    }

}
