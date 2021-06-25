package Client.Controller;

import Client.Layouts.Layout;
import Client.Modell.Lehrender;
import Client.Modell.Lehrveranstaltung;
import Client.Modell.QuizAnswer;
import Client.Modell.QuizQuestion;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;


public class CreateQuizController {
    @FXML
    private Button newQuestion;
    @FXML
    private Button create;
    @FXML
    private TextField questionField;
    @FXML
    private TextField answerField;
    @FXML
    private Button newAnswer;
    @FXML
    private CheckBox correct;
    @FXML
    private TextField quiz_titel;


    private HashMap<String, Boolean> answers;
    private HashMap<String, HashMap<String, Boolean>> questions;
    private Object nutzer;
    private Lehrveranstaltung lehrveranstaltung;
    private Layout layout;


    public void initialize() {
        answers= new HashMap<>();
        questions = new HashMap<>();

    }


    public void setNutzer(Object nutzer, Lehrveranstaltung lehrveranstaltung) {
        this.nutzer=nutzer;
        this.lehrveranstaltung=lehrveranstaltung;
    }


    public void pressedNewQuestion(ActionEvent actionEvent) {
        if(answers.size() > 0) {
            if (questionField.getText() != ""
                    && !questionField.getText().contains(",")
                    && !questionField.getText().contains(":")
                    && !questionField.getText().contains("{")
                    && !questionField.getText().contains("}")) {
                if (answerField.getText() != "") {
                    answers.put(answerField.getText(), correct.isSelected());
                    correct.setSelected(false);
                }
                answerField.setText("");
                correct.setSelected(false);
                questions.put(questionField.getText(), answers);

                questionField.setText("");
                answers = new HashMap<>();
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

                String url = "http://localhost:8080/quiz/createQuiz";
                HttpPost post = new HttpPost(url);
                MultipartEntityBuilder entity = MultipartEntityBuilder.create();
                entity.addTextBody("titel", quiz_titel.getText());
                entity.addTextBody("lehrenderId", String.valueOf(((Lehrender) nutzer).getId()));
                entity.addTextBody("lehrveranstaltungsId", String.valueOf(lehrveranstaltung.getId()));

                HttpEntity requestEntity = entity.build();
                post.setEntity(requestEntity);

                try (CloseableHttpResponse response = client.execute(post)) {
                    HttpEntity responseEntity = response.getEntity();
                    String result = EntityUtils.toString(responseEntity);
//                    System.out.println(result);
                    if(result.contains("OK:")) {
                        try (CloseableHttpClient client1 = HttpClients.createDefault()) {

                            String url1 = "http://localhost:8080/quiz/createQuestion";
                            HttpPost post1 = new HttpPost(url1);
                            MultipartEntityBuilder entity1 = MultipartEntityBuilder.create();
                            entity1.addTextBody("quiz", result.split(":")[1]);
                            int index = 0;
                            for(Map.Entry<String, HashMap<String, Boolean>> entry : questions.entrySet()) {
                                String question = entry.getKey();
                                HashMap<String, Boolean> answers = entry.getValue();
                                String frage = question+";";
                                for(Map.Entry<String, Boolean> entry1 : answers.entrySet()){
                                    frage += entry1.getKey()+";"+entry1.getValue()+";";
                                }
                                entity1.addTextBody("question", frage);
                            }

                            HttpEntity requestEntity1 = entity1.build();
                            post1.setEntity(requestEntity1);

                            try (CloseableHttpResponse response1 = client1.execute(post1)) {
                                HttpEntity responseEntity1 = response1.getEntity();
                                String result1 = EntityUtils.toString(responseEntity1);
//                                System.out.println("Fragen gesendet!");
                                if(result1.contains("OK")) {

                                    layout.instanceLayout("quizUebersicht.fxml");
                                    ((QuizUebersichtController) layout.getController()).setLayout(layout);
                                    ((QuizUebersichtController) layout.getController()).quizSeiteAufrufen(nutzer, lehrveranstaltung);

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
            fehler.setTitle("Quiz kann nicht erstellt werden!");
            fehler.setContentText("Sie müssen mindestens eine Frage samt Antworten hinzugefügt haben!");
            fehler.showAndWait();
        }
    }

    public void pressedNewAnswer(ActionEvent actionEvent) {
        if (answerField.getText() != ""
                && !questionField.getText().contains(",")
                && !questionField.getText().contains(":")
                && !questionField.getText().contains("{")
                && !questionField.getText().contains("}")) {
            answers.put(answerField.getText(), correct.isSelected());
            answerField.setText("");
            correct.setSelected(false);
        }else{
            Alert fehler = new Alert(Alert.AlertType.ERROR);
            fehler.setTitle("Antwort Eingabe ungültig!");
            fehler.setContentText("Antwort darf nicht leer sein und nicht { } , : enthalten!");
            fehler.showAndWait();
        }
    }

    public void isCorrect(ActionEvent actionEvent) {
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }
}
