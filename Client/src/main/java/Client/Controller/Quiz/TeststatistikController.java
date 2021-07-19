package Client.Controller.Quiz;

import Client.Controller.Quiz.QuizUebersichtController;
import Client.Layouts.Layout;
import Client.Modell.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class TeststatistikController {
    @FXML
    public PieChart passed;
    @FXML
    public TableView<QuizQuestion> table_korrekt;
    @FXML
    public TableColumn<QuizQuestion, String> korrekteAnzahl;
    @FXML
    public TableColumn<QuizQuestion, String> frage;
    @FXML
    public TableView<Nutzer> table_versuch;
    @FXML
    public TableColumn<Nutzer, String> student;
    @FXML
    public TableColumn<Nutzer, String> versuch;
    @FXML
    public Label erfolg;
    @FXML
    public Label failed;
    @FXML
    public Button back;
    @FXML
    public Label quizname_Label;
    @FXML
    private Label beteiligung;

    public Object getNutzer() {
        return nutzer;
    }

    public void setNutzer(Object nutzer) {
        this.nutzer = nutzer;
    }

    private Object nutzer;

    public Lehrveranstaltung getLehrveranstaltung() {
        return lehrveranstaltung;
    }

    public void setLehrveranstaltung(Lehrveranstaltung lehrveranstaltung) {
        this.lehrveranstaltung = lehrveranstaltung;
    }

    private Lehrveranstaltung lehrveranstaltung;

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    private Quiz quiz;

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    private Layout layout;


    public void showPieChart() {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/quiz/bestehensquote/"+ quiz.getId())).build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            int bestanden = Integer.parseInt(response.body());
            int gesamt = gesamteAnzahl();
            int durchgefallen = gesamt - bestanden;

            double best_double =  bestanden;
            double durch_double = durchgefallen;
            double gesamt_double= gesamt;

            double durchgefallen_prozent =durch_double/gesamt_double*100;
            double bestanden_prozent = best_double/gesamt_double*100;

            double durch = Math.floor(durchgefallen_prozent * 100) / 100;
            double erf = Math.floor(bestanden_prozent * 100) / 100;

            erfolg.setText("Es hat/haben "+ bestanden + " ("+erf+ "%) Person/en bestanden");
            failed.setText("Es ist/sind "+ durchgefallen + " ("+durch+ "%) Person/en durchgefallen");

            ObservableList<PieChart.Data> pieChartData =
                    FXCollections.observableArrayList(
                            new PieChart.Data("Bestanden",bestanden),
                            new PieChart.Data("Durchgefallen", durchgefallen)
                    );



            passed.setData(pieChartData);
            passed.setTitle("Bestehensquote");
            passed.getData().get(0).getNode().setStyle("-fx-pie-color: green");
            passed.getData().get(1).getNode().setStyle("-fx-pie-color: red");
            passed.setLegendVisible(false);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void teilnahme() {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/quiz/alleStudenten/"+ quiz.getId())).build();
        HttpResponse<String> response;


        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int teilgenommen = Integer.parseInt(response.body());
            int alle = alleStudentenDesKurses();

            beteiligung.setText("Es haben ingesamt "+teilgenommen+" von " + alle+ " an den Tests teilgenommen.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int alleStudentenDesKurses(){
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/quiz/alleTeilnehmer/"+ lehrveranstaltung.getId())).build();
        HttpResponse<String> response= null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(response.body());
    }


    public void populateTableviewVersuch() {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/quiz/versuche/"+ quiz.getId())).build();
        HttpResponse<String> response1 = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            response1 = client.send(request, HttpResponse.BodyHandlers.ofString());
            java.util.List<Object[]> objects = mapper.readValue(response1.body(), new TypeReference<List<Object[]>>() {});
            List<Nutzer> nutzerliste = new ArrayList<>();
            JSONArray neu = new JSONArray(response1.body());
           for(int i =0; i <neu.length(); i++) {

               JSONObject json= neu.getJSONArray(i).getJSONObject(0);
               int versuche = neu.getJSONArray(i).getInt(1);

               Nutzer nutzer = new Nutzer();
               nutzer.addDataFromJson(json);
               nutzer.setVersuche(versuche);
               nutzerliste.add(nutzer);
           }

            student.setCellValueFactory(new PropertyValueFactory<>("name"));
            versuch.setCellValueFactory(new PropertyValueFactory<>("versuche"));
           ObservableList<Nutzer> obsLv = FXCollections.observableList(nutzerliste);
            table_versuch.setItems(obsLv);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void populateTableviewKorrekt() {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/quiz/anzahl/"+ quiz.getId())).build();
        HttpResponse<String> response1 = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            response1 = client.send(request, HttpResponse.BodyHandlers.ofString());
            java.util.List<Integer> objects = mapper.readValue(response1.body(), new TypeReference<List<Integer>>() {});
            List<QuizQuestion> count =  new ArrayList<>();
            List<String> kurse = stelleFragenDar();

            for(int i =0; i< objects.size(); i++) {
                QuizQuestion quizquestion = new QuizQuestion();
               quizquestion.setAnzahlKorrekt(objects.get(i));
               quizquestion.setQuestion(kurse.get(i));
               count.add(quizquestion);
            }
            frage.setCellValueFactory(new PropertyValueFactory<>("question"));
            korrekteAnzahl.setCellValueFactory(new PropertyValueFactory<>("anzahlKorrekt"));
            ObservableList<QuizQuestion> obsLv = FXCollections.observableList(count);
            table_korrekt.setItems(obsLv);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<String> stelleFragenDar() {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/quiz/anzahlKorrekt/" + quiz.getId())).build();
        HttpResponse<String> response2 = null;
        List<String> RichtigeListe = null;
        try {
            response2 = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONArray neu = new JSONArray(response2.body());
            String[] answerArray = new String[neu.length()];
            for (int j = 0; j < neu.length(); j++) {

                answerArray[j] = String.valueOf(neu.get(j));
            }

            RichtigeListe = new ArrayList<>();
            RichtigeListe.add(answerArray[0]);

            for (int q = 0; q < answerArray.length; q++) {

                boolean drin = vorhandenChecker(RichtigeListe, answerArray[q]);

                if (drin == false) {
                    RichtigeListe.add(answerArray[q]);
                }

            }
                List<String> endListe = new ArrayList<>();
            for (int j=0; j<RichtigeListe.size(); j++) {
                String string =RichtigeListe.get(j).replace("[","").replace("]", "");
                endListe.add(string);
            }
            RichtigeListe = endListe;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return RichtigeListe;
    }
    public boolean vorhandenChecker(List<String> hilfsListe,String string) {
        int counter =0;
        for (int h =0; h< hilfsListe.size(); h++) {
            if(string.equals(hilfsListe.get(h))) {
                counter++;
            }
        }
        if (counter== 0) {
            return false;
        }
       return true;
    }

    public int gesamteAnzahl(){
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/quiz/alleStudenten/"+ quiz.getId())).build();
        HttpResponse<String> response1 = null;

        try {
            response1 = client.send(request, HttpResponse.BodyHandlers.ofString());
            
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(response1.body());
    }

    public void pressedBack(ActionEvent actionEvent) {
        actionEvent.consume();
        layout.instanceLayout("quizUebersicht.fxml");
        ((QuizUebersichtController) layout.getController()).setLayout(layout);
        ((QuizUebersichtController) layout.getController()).quizSeiteAufrufen(nutzer, lehrveranstaltung);
        ((QuizUebersichtController) layout.getController()).quizerstellen_LvTitel_Label.setText("Lehrveranstaltung " + lehrveranstaltung.getTitel() );


    }
}
