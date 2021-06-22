package Client.Controller;

import Client.Modell.*;
import com.fasterxml.jackson.annotation.JacksonInject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TeststatistikController {
    @FXML
    public PieChart passed;
    @FXML
    public TableView table_korrekt;
    @FXML
    public TableColumn frage;
    @FXML
    public TableColumn antwort;
    @FXML
    public TableView table_versuch;
    @FXML
    public TableColumn student;
    @FXML
    public TableColumn versuch;
    @FXML
    public Label erfolg;
    @FXML
    public Label failed;
    @FXML
    private Label beteiligung;

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


    public void showPieChart() {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/quiz/bestehensquote/"+ quiz.getId())).build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
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
                            new PieChart.Data("Bestanden", bestanden),
                            new PieChart.Data("Durchgefallen", durchgefallen)
                    );
            passed.setData(pieChartData);
            passed.setTitle("Bestehensquote");



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

            beteiligung.setText("Es haben ingesamt "+teilgenommen+" von" + alle+ " an den Tests teilgenommen.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public int alleStudentenDesKurses(){
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/quiz/bestehensquote/"+ quiz.getId())).build();
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



}
