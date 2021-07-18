package Client.Controller.Quiz;

import Client.Controller.Quiz.QuizUebersichtController;
import Client.Layouts.Layout;
import Client.Modell.Lehrender;
import Client.Modell.Lehrveranstaltung;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class CreateQuizController {
    @FXML
    public Label quiz_LvTitel_Label;
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
            if (questionTextArea.getText() != ""
                    && !questionTextArea.getText().contains(",")
                    && !questionTextArea.getText().contains(":")
                    && !questionTextArea.getText().contains("{")
                    && !questionTextArea.getText().contains("}")) {
                if (answerTextArea.getText() != "") {
                    answers.put(answerTextArea.getText(), correct.isSelected());
                    correct.setSelected(false);
                }
                answerTextArea.setText("");
                correct.setSelected(false);
                questions.put(questionTextArea.getText(), answers);

                questionTextArea.setText("");
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
                                    ((QuizUebersichtController) layout.getController()).quizerstellen_LvTitel_Label.setText("Lehrveranstaltung " + lehrveranstaltung.getTitel() );

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
//        System.out.println("Eingegebene Antwortmöglichkeit: " + answerTextArea.getText());
        if (answerTextArea.getText() != ""
                    && !questionTextArea.getText().contains(",")
                    && !questionTextArea.getText().contains(":")
                    && !questionTextArea.getText().contains("{")
                    && !questionTextArea.getText().contains("}")) {
            answers.put(answerTextArea.getText(), correct.isSelected());
            answerTextArea.setText("");
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
