package Client.Controller.Review;

import Client.Layouts.Layout;
import Client.Modell.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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
import java.util.List;

public class ReviewBearbeitenController {
    @FXML
    public VBox answers;
    @FXML
    public ProgressBar loadingBar;
    @FXML
    public Text reviewTitel;
    @FXML
    public Text questionAnzahl;
    @FXML
    public Text Question;
    @FXML
    public Button nextQuestion;
    public AnchorPane panel;
    public VBox questionVbox;

    private Layout layout;
    private Object nutzer;
    private Lehrveranstaltung lehrveranstaltung;
    private Review review;
    private int reviewIndex = 0;
    private List<ReviewQuestion> fragen;
    private List<ReviewAnswer> antworten;
    private List<CheckBox> checkboxanswers;
    private List<String> Feedback = new ArrayList<>();
 //   private int korrekteFragen = 0;

    public void setUpReview(){
        reviewTitel.setText("Review: "+ review.getTitel());
        checkboxanswers = new ArrayList<>();
        //Fragen herunterladen
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/review/alleFragen/"+ lehrveranstaltung.getId())).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            fragen =  mapper.readValue(response.body(), new TypeReference<List<ReviewQuestion>>() {});
          System.out.println("FRAGEN       " +fragen);
            LoadAnswers();

            if (reviewIndex +1 == fragen.size()) {
//                    System.out.println("Review ende");
                nextQuestion.setText("Review beenden");
                nextQuestion.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        CheckAntwort();
                        panel.getChildren().clear();
                        panel.getChildren().add(reviewTitel);
                        Question.setText("Vielen Dank für Ihre Teilnahme!");
                        for (int h = 0; h < Feedback.size(); h++) {
                            Text FeedbackText = new Text();
                            FeedbackText.setText(Feedback.get(h));
                            FeedbackText.setFont(new Font(18.0));
                            FeedbackText.setStyle("-fx-padding: 10 0 0 10; -fx-background-radius: 10.0;");
                            questionVbox.getChildren().add(FeedbackText);
                        }

                        try (CloseableHttpClient client1 = HttpClients.createDefault()) {

                            String url1 = "http://localhost:8080/review/bearbeitetReview";
                            HttpPost post1 = new HttpPost(url1);
                            MultipartEntityBuilder entity1 = MultipartEntityBuilder.create();
                            entity1.addTextBody("nutzerId", String.valueOf(((Student) nutzer).getNutzer().getId()));
                            entity1.addTextBody("reviewId", String.valueOf(review.getId()) );

                            HttpEntity requestEntity1 = entity1.build();
                            post1.setEntity(requestEntity1);

                            try (CloseableHttpResponse response1 = client1.execute(post1)) {
                                HttpEntity responseEntity1 = response1.getEntity();
                                String result1 = EntityUtils.toString(responseEntity1);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        panel.getChildren().add(questionVbox);
                    }
                });
            }
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void LoadAnswers() {
        Question.setText(fragen.get(reviewIndex).getQuestion());
        questionAnzahl.setText("Frage "+ (reviewIndex+1) + " von "+ fragen.size());
        double progessbar =  ((double) reviewIndex / (double) fragen.size());
        loadingBar.setProgress(progessbar);

        //Antworten herunterladen
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/review/alleAntworten/"+ fragen.get(reviewIndex).getId())).build();
        HttpResponse<String> response = null;
//        System.out.println("Load Answers vorher  " +fragen.get(reviewIndex).getId());
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            antworten =  mapper.readValue(response.body(), new TypeReference<List<ReviewAnswer>>() {});
//            System.out.println("ANTWORTEN    " +antworten);
            for(int i = 0; i < antworten.size(); i++){
                addAnswer(antworten.get(i).getAnswer());
            }

        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void addAnswer(String answer) {
        CheckBox check = new CheckBox();
        check.setStyle("-fx-padding: 10 0 0 10; -fx-background-radius: 10.0;");
        check.setWrapText(true);
        check.setAlignment(Pos.CENTER_LEFT);
        check.setText(answer);
        check.setFont(new Font(18.0));
        answers.setPadding(new Insets(10,0,10,0));
        answers.getChildren().add(check);
        checkboxanswers.add(check);
    }

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public Object getNutzer() {
        return nutzer;
    }

    public void setNutzer(Object nutzer) {
        this.nutzer = nutzer;
    }

    public Lehrveranstaltung getLehrveranstaltung() {
        return lehrveranstaltung;
    }

    public void setLehrveranstaltung(Lehrveranstaltung lehrveranstaltung) {
        this.lehrveranstaltung = lehrveranstaltung;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
        setUpReview();
    }

    public boolean CheckAntwort(){
        boolean mindestenseinCheckBoxausgewählt = false;
        for (int i = 0; i < checkboxanswers.size(); i++) {
            if (checkboxanswers.get(i).isSelected()) {
                mindestenseinCheckBoxausgewählt = true;
                break;
            }
        }
        if (!mindestenseinCheckBoxausgewählt) {
            Alert fehler = new Alert(Alert.AlertType.ERROR);
            fehler.setTitle("Sie können nicht zur nächsten Frage wechseln");
            fehler.setContentText("Sie müssen mindestens eine Antwort auswählen");
            fehler.showAndWait();
            return false;
        } else {

            try (CloseableHttpClient client1 = HttpClients.createDefault()) {


                String url1 = "http://localhost:8080/review/bearbeitetReviewQuestion";
                HttpPost post1 = new HttpPost(url1);
                MultipartEntityBuilder entity1 = MultipartEntityBuilder.create();
                entity1.addTextBody("nutzerId", String.valueOf(((Student) nutzer).getId()));
                entity1.addTextBody("questionId", String.valueOf(fragen.get(reviewIndex).getId()) );
                for (int j = 0; j < antworten.size(); j++) {
                    if (checkboxanswers.get(j).isSelected()) {
                        entity1.addTextBody("answer",String.valueOf(antworten.get(j).getId()));


                    }
                }

                HttpEntity requestEntity1 = entity1.build();
                post1.setEntity(requestEntity1);

                try (CloseableHttpResponse response1 = client1.execute(post1)) {
                    HttpEntity responseEntity1 = response1.getEntity();
                    String result1 = EntityUtils.toString(responseEntity1);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    public void nextQuestionMethod(ActionEvent actionEvent) {
        if(CheckAntwort()) {
            int nextQuestIndex = reviewIndex + 1;


            if (nextQuestIndex < fragen.size()) {
                reviewIndex++;
                antworten = new ArrayList<>();
                answers.getChildren().clear();
                checkboxanswers.clear();
                LoadAnswers();


                if (nextQuestIndex +1 == fragen.size()) {
//                    System.out.println("Review ende");
                    nextQuestion.setText("Review beenden");
                    nextQuestion.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            CheckAntwort();
                            panel.getChildren().clear();
                            panel.getChildren().add(reviewTitel);
                            Question.setText("Vielen Dank für Ihre Teilnahme!");
                            for (int h = 0; h < Feedback.size(); h++) {
                                Text FeedbackText = new Text();
                                FeedbackText.setText(Feedback.get(h));
                                FeedbackText.setFont(new Font(18.0));
                                FeedbackText.setStyle("-fx-padding: 10 0 0 10; -fx-background-radius: 10.0;");
                                questionVbox.getChildren().add(FeedbackText);
                            }

                            try (CloseableHttpClient client1 = HttpClients.createDefault()) {

                                String url1 = "http://localhost:8080/review/bearbeitetReview";
                                HttpPost post1 = new HttpPost(url1);
                                MultipartEntityBuilder entity1 = MultipartEntityBuilder.create();
                                entity1.addTextBody("nutzerId", String.valueOf(((Student) nutzer).getNutzer().getId()));
                                entity1.addTextBody("reviewId", String.valueOf(review.getId()) );

                                HttpEntity requestEntity1 = entity1.build();
                                post1.setEntity(requestEntity1);

                                try (CloseableHttpResponse response1 = client1.execute(post1)) {
                                    HttpEntity responseEntity1 = response1.getEntity();
                                    String result1 = EntityUtils.toString(responseEntity1);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            panel.getChildren().add(questionVbox);
                        }
                    });
                }
            }
        }
    }

}
